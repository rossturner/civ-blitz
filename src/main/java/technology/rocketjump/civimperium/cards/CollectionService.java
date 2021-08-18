package technology.rocketjump.civimperium.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.Collection;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.matches.MatchRepo;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.CollectionCard;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.util.*;

@Service
public class CollectionService {

	private static final Integer NUM_INITIAL_CARDS_PER_CATEGORY = 4;
	public static final int MAX_MULLIGANS_ALLOWED = 4;
	private final SourceDataRepo sourceDataRepo;
	private final CollectionRepo collectionRepo;
	private final MatchRepo matchRepo;
	private final Random random = new Random();

	@Autowired
	public CollectionService(SourceDataRepo sourceDataRepo, CollectionRepo collectionRepo, MatchRepo matchRepo) {
		this.sourceDataRepo = sourceDataRepo;
		this.collectionRepo = collectionRepo;
		this.matchRepo = matchRepo;
	}

	public List<Collection> initialiseCollection(Player player, int timesMulliganed) {
		if (timesMulliganed > MAX_MULLIGANS_ALLOWED) {
			throw new IllegalArgumentException("Can not mulligan more than 4 times");
		}
		if (matchRepo.getNumActiveMatches(player) > 0) {
			throw new IllegalStateException("Can not mulligan after having started a match");
		}

		collectionRepo.deleteCollection(player);

		List<CardCategory> categories = new ArrayList<>(List.of(CardCategory.values()));
		Collections.shuffle(categories); // shuffle categories so we can randomly remove up to 1 from each
		Map<CardCategory, Integer> cardsToSelect = new HashMap<>();
		for (CardCategory category : categories) {
			int numCardsToSelect = NUM_INITIAL_CARDS_PER_CATEGORY;
			if (timesMulliganed > 0) {
				numCardsToSelect--;
				timesMulliganed--;
			}
			cardsToSelect.put(category, numCardsToSelect);
		}

		for (Map.Entry<CardCategory, Integer> entry : cardsToSelect.entrySet()) {
			Set<Card> selectedForCategory = new HashSet<>();
			List<Card> cardsInCategory = sourceDataRepo.getByCategory(entry.getKey());
			while (selectedForCategory.size() < entry.getValue()) {
				Card randomCard = cardsInCategory.get(random.nextInt(cardsInCategory.size()));
				selectedForCategory.add(randomCard);
			}

			for (Card card : selectedForCategory) {
				collectionRepo.addToCollection(card, player);
			}
		}

		return collectionRepo.getCollection(player);
	}

	public int getTimesMulliganed(Player player) {
		if (matchRepo.getNumActiveMatches(player) > 0) {
			return MAX_MULLIGANS_ALLOWED;
		} else {
			int defaultInitialCards = (NUM_INITIAL_CARDS_PER_CATEGORY * CardCategory.values().length);
			return defaultInitialCards - getCollection(player).size();
		}
	}

	public List<CollectionCard> getCollection(Player player) {
		List<Collection> collectionList = collectionRepo.getCollection(player);
		List<CollectionCard> result = new ArrayList<>(collectionList.size());
		for (Collection collectionEntry : collectionList) {
			Card card = sourceDataRepo.getByTraitType(collectionEntry.getCardTraitType());
			CollectionCard collectionCard = new CollectionCard(card, collectionEntry.getQuantity());
			if (card.getGrantsFreeUseOfCard().isPresent()) {
				collectionCard.setFreeUseCard(sourceDataRepo.getByTraitType(card.getGrantsFreeUseOfCard().get()));
			}
			result.add(collectionCard);
		}
		return result;
	}

	public void addToCollection(Card card, Player player) {
		collectionRepo.addToCollection(card, player);
	}

	public void removeFromCollection(Card card, Player player) {
		collectionRepo.removeFromCollection(card, player);
	}

}
