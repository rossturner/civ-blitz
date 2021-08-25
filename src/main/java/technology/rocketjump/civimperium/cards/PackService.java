package technology.rocketjump.civimperium.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.codegen.tables.pojos.CardPack;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static technology.rocketjump.civimperium.model.CardCategory.*;

@Service
public class PackService {

	private final PackRepo packRepo;
	private final SourceDataRepo sourceDataRepo;
	private final CollectionService collectionService;
	private final Random random = new Random();

	@Autowired
	public PackService(PackRepo packRepo, SourceDataRepo sourceDataRepo, CollectionService collectionService) {
		this.packRepo = packRepo;
		this.sourceDataRepo = sourceDataRepo;
		this.collectionService = collectionService;
	}

	public void addMatchBooster(MatchSignupWithPlayer player) {
		CardPack pack = new CardPack();
		pack.setPlayerId(player.getPlayerId());
		pack.setPackType(CardPackType.MATCH_BOOSTER);

		pack.setNumCivAbility(player.getCivAbilityIsFree() ? 0 : 1);
		pack.setNumLeaderAbility(player.getLeaderAbilityIsFree() ? 0 : 1);
		pack.setNumUniqueInfrastructure(player.getUniqueInfrastructureIsFree() ? 0 : 1);
		pack.setNumUniqueUnit(player.getUniqueUnitIsFree() ? 0 : 1);

		packRepo.create(pack);
	}

	public List<CardPack> getAllPacks(Player player) {
		return packRepo.getAllForPlayer(player);
	}

	public synchronized List<Card> openPack(CardPack pack, Player player) {
		if (packRepo.get(pack.getPackId()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Pack no longer exists");
		}

		List<Card> selectedCards = new ArrayList<>();

		// randomly select cards per category
		for (int cursor = 0; cursor < pack.getNumCivAbility(); cursor++) {
			selectCard(CivilizationAbility, selectedCards);
		}
		for (int cursor = 0; cursor < pack.getNumLeaderAbility(); cursor++) {
			selectCard(LeaderAbility, selectedCards);
		}
		for (int cursor = 0; cursor < pack.getNumUniqueInfrastructure(); cursor++) {
			selectCard(UniqueInfrastructure, selectedCards);
		}
		for (int cursor = 0; cursor < pack.getNumUniqueUnit(); cursor++) {
			selectCard(UniqueUnit, selectedCards);
		}

		for (Card card : selectedCards) {
			collectionService.addToCollection(card, player);
		}

		packRepo.delete(pack);

		return selectedCards;
	}

	private void selectCard(CardCategory cardCategory, List<Card> selectedCards) {
		List<Card> allInCategory = sourceDataRepo.getByCategory(cardCategory);
		Card selected = null;
		while (selected == null) {
			selected = allInCategory.get(random.nextInt(allInCategory.size()));
			if (selectedCards.contains(selected)) {
				selected = null;
			}
		}
		selectedCards.add(selected);
	}
}
