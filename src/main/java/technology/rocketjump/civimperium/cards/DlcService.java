package technology.rocketjump.civimperium.cards;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.PlayerDlcSetting;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CollectionCard;
import technology.rocketjump.civimperium.model.PlayerDlcResponse;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DlcService {

	private static final String CIV_6_BASE_GAME = "Civilization VI Base Game";
	private static final String GATHERING_STORM_EXPANSION = "Gathering Storm expansion";
	private final SourceDataRepo sourceDataRepo;
	private final DlcRepo dlcRepo;
	private final CollectionService collectionService;

	private final List<String> allDlcNames = new ArrayList<>();

	@Autowired
	public DlcService(SourceDataRepo sourceDataRepo, DlcRepo dlcRepo, CollectionService collectionService) {
		this.sourceDataRepo = sourceDataRepo;
		this.dlcRepo = dlcRepo;
		this.collectionService = collectionService;
	}

	public List<PlayerDlcResponse> getPlayerSettings(Player player) {
		if (allDlcNames.isEmpty()) {
			initialise();
		}
		List<PlayerDlcSetting> playerSettings = dlcRepo.getAllForPlayer(player);

		List<PlayerDlcResponse> response = new ArrayList<>();
		for (String dlcName : allDlcNames) {
			boolean dlcIsEnabled = dlcName.equals(CIV_6_BASE_GAME) || dlcName.equals(GATHERING_STORM_EXPANSION) ||
					playerSettings.stream().anyMatch(s -> s.getDlcName().equals(dlcName));
			response.add(new PlayerDlcResponse(dlcName, dlcIsEnabled));
		}

		return response;
	}

	public List<CollectionCard> getCardsToBeRerolled(Player player, List<String> selectedDlcNames) {
		if (allDlcNames.isEmpty()) {
			initialise();
		}

		List<PlayerDlcSetting> currentSettings = dlcRepo.getAllForPlayer(player);

		List<String> deselectedDlcNames = currentSettings.stream().filter(s -> !selectedDlcNames.contains(s.getDlcName()))
				.map(s -> s.getDlcName())
				.collect(Collectors.toList());
		if (!deselectedDlcNames.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not remove DLC you have previously selected ("+ StringUtils.join(deselectedDlcNames, ", ")+")");
		}

		List<String> unsupportedDlc = new ArrayList<>(allDlcNames);
		unsupportedDlc.removeAll(selectedDlcNames);

		List<CollectionCard> collection = collectionService.getCollection(player);

		List<CollectionCard> cardsToBeRerolled = new ArrayList<>();
		for (CollectionCard collectionCard : collection) {
			if (unsupportedDlc.contains(collectionCard.getRequiredDlc())) {
				cardsToBeRerolled.add(collectionCard);
			}
		}

		return cardsToBeRerolled;
	}

	public void updateSettings(Player player, List<String> selectedDlcNames) {
		// this method handles dealing with removal of previously selected settings
		List<CollectionCard> cardsToBeRerolled = getCardsToBeRerolled(player, selectedDlcNames);
		List<PlayerDlcSetting> currentSettings = dlcRepo.getAllForPlayer(player);

		for (String selectedDlcName : selectedDlcNames) {
			if (!currentSettings.stream().anyMatch(s -> s.getDlcName().equals(selectedDlcName))) {
				// new setting to be added
				dlcRepo.addSetting(player, selectedDlcName);
			}
		}

		currentSettings = dlcRepo.getAllForPlayer(player);

		for (CollectionCard collectionCard : cardsToBeRerolled) {
			collectionService.rerollCard(collectionCard, player, currentSettings);
		}
	}

	private synchronized void initialise() {
		if (allDlcNames.isEmpty()) {
			for (Card card : sourceDataRepo.getAll()) {
				String requiredDlc = card.getRequiredDlc();
				if (requiredDlc != null && !allDlcNames.contains(requiredDlc)) {
					allDlcNames.add(requiredDlc);
				}
			}
			Collections.sort(allDlcNames);
		}
	}
}
