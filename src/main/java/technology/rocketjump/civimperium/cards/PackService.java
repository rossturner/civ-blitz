package technology.rocketjump.civimperium.cards;

import org.apache.commons.lang3.EnumUtils;
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
import technology.rocketjump.civimperium.players.PlayerRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static technology.rocketjump.civimperium.model.CardCategory.*;

@Service
public class PackService {

	private final PackRepo packRepo;
	private final SourceDataRepo sourceDataRepo;
	private final CollectionService collectionService;
	private final PlayerRepo playerRepo;
	private final Random random = new Random();

	@Autowired
	public PackService(PackRepo packRepo, SourceDataRepo sourceDataRepo, CollectionService collectionService, PlayerRepo playerRepo) {
		this.packRepo = packRepo;
		this.sourceDataRepo = sourceDataRepo;
		this.collectionService = collectionService;
		this.playerRepo = playerRepo;
	}

	public void purchasePack(Player player, CardPackType packType, String category) {
		if (player.getBalance() < packType.cost) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not afford this pack");
		}

		CardPack newPack = new CardPack();
		newPack.setPlayerId(player.getPlayerId());
		newPack.setPackType(packType);
		switch (packType) {
			case SINGLE_CARD:
				setNumberOfCards(newPack, category, 1);
				break;
			case MULTIPLE_CARDS:
				setNumberOfCards(newPack, category, 3);
				break;
			case MATCH_BOOSTER:
				for (CardCategory cardCategory : values()) {
					setNumberOfCards(newPack, cardCategory.name(), 1);
				}
				break;
			default:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Pack type not yet implemented: " + packType);
		}
		packRepo.create(newPack);

		player.setBalance(player.getBalance() - packType.cost);
		playerRepo.updateBalances(player);
	}

	private void setNumberOfCards(CardPack pack, String category, int numtoAdd) {
		CardCategory cardCategory = EnumUtils.getEnum(CardCategory.class, category);
		if (cardCategory == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unrecognised category: " + category);
		}

		switch (cardCategory) {
			case CivilizationAbility:
				pack.setNumCivAbility(numtoAdd);
				break;
			case LeaderAbility:
				pack.setNumLeaderAbility(numtoAdd);
				break;
			case UniqueInfrastructure:
				pack.setNumUniqueInfrastructure(numtoAdd);
				break;
			case UniqueUnit:
				pack.setNumUniqueUnit(numtoAdd);
				break;
			default:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not implemented category: " + category);
		}
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
		if (pack.getNumCivAbility() != null) {
			for (int cursor = 0; cursor < pack.getNumCivAbility(); cursor++) {
				selectCard(CivilizationAbility, selectedCards);
			}
		}
		if (pack.getNumLeaderAbility() != null) {
			for (int cursor = 0; cursor < pack.getNumLeaderAbility(); cursor++) {
				selectCard(LeaderAbility, selectedCards);
			}
		}
		if (pack.getNumUniqueInfrastructure() != null) {
			for (int cursor = 0; cursor < pack.getNumUniqueInfrastructure(); cursor++) {
				selectCard(UniqueInfrastructure, selectedCards);
			}
		}
		if (pack.getNumUniqueUnit() != null) {
			for (int cursor = 0; cursor < pack.getNumUniqueUnit(); cursor++) {
				selectCard(UniqueUnit, selectedCards);
			}
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
