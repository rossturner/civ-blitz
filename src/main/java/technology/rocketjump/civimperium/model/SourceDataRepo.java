package technology.rocketjump.civimperium.model;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SourceDataRepo {

	private final Map<String, String> friendlyNameByCivilizationType = new TreeMap<>();
	private final Map<String, Card> cardsByTraitType = new TreeMap<>();
	private final Map<String, Card> cardsByCardName = new TreeMap<>();
	private final Map<CardCategory, List<Card>> cardsByCategory = new EnumMap<>(CardCategory.class);
	private final Map<String, String> subtypesByTraitType = new HashMap<>();
	private final Map<String, IconAtlasEntry> iconAtlasEntriesByCivOrLeaderType = new HashMap<>();

	public final Map<String, String> leaderNameByLeaderType = new HashMap<>();
	public final Map<String, String> leaderIconByLeaderType = new HashMap<>();
	public final Map<String, String> leaderAbilityIconByLeaderType = new HashMap<>();
	public final Map<String, String> civNameByCivType = new HashMap<>();
	public final Map<String, String> civIconByCivType = new HashMap<>();
	public final Map<String, String> civAbilityNameByCivType = new HashMap<>();
	public final Map<String, String> civAbilityDescByCivType = new HashMap<>();
	public final Map<String, String> civAbilityIconByCivType = new HashMap<>();
	public final Map<String, String> leaderTraitNameByTraitType = new HashMap<>();
	public final Map<String, String> leaderTraitDescByTraitType = new HashMap<>();
	public final Map<String, String> portraitsByLeaderType = new HashMap<>();
	public final Map<String, String> portraitBackgroundsByLeaderType = new HashMap<>();
	public final Map<String, String> capitalNamesByCivType = new HashMap<>();

	public final Map<String, CSVRecord> civilizationCsvRecordsByCivType = new HashMap<>();

	@Autowired
	public SourceDataRepo() {

	}

	public void addCivFriendlyName(String civilizationType, String civFriendlyName) {
		friendlyNameByCivilizationType.put(civilizationType, civFriendlyName);
	}

	public void add(Card card) {
		if (cardsByTraitType.containsKey(card.getTraitType())) {
			System.out.println("Duplicate card for trait type " + card.getTraitType());
		}

		cardsByTraitType.put(card.getTraitType(), card);
		cardsByCardName.put(card.getCardName(), card);
		cardsByCategory.computeIfAbsent(card.getCardCategory(), a -> new ArrayList<>()).add(card);
	}

	public void removeGrantedCards() {
		for (String traitType : new ArrayList<>(cardsByTraitType.keySet())) {
			Card card = cardsByTraitType.get(traitType);
			if (card != null && card.getGrantsTraitType().isPresent()) {
				Card removed = cardsByTraitType.remove(card.getGrantsTraitType().get());
				cardsByCardName.remove(card.getCardName());
				cardsByCategory.get(removed.getCardCategory()).remove(removed);
			}
		}

	}

	public Collection<Card> getAll() {
		return cardsByTraitType.values();
	}

	public String getFriendlyCivName(String civilizationType) {
		return friendlyNameByCivilizationType.get(civilizationType);
	}

	public Card getByTraitType(String traitType) {
		return cardsByTraitType.get(traitType);
	}

	public List<Card> getByCategory(CardCategory cardCategory) {
		return cardsByCategory.get(cardCategory);
	}

	public void addSubtypeByTraitType(String traitType, String subtype) {
		subtypesByTraitType.put(traitType, subtype);
	}

	public String getSubtypeByTraitType(String traitType) {
		return subtypesByTraitType.get(traitType);
	}

	public void addIconAtlasEntry(String civOrLeaderType, IconAtlasEntry iconAtlasEntry) {
		iconAtlasEntriesByCivOrLeaderType.put(civOrLeaderType, iconAtlasEntry);
	}

	public IconAtlasEntry getIconAtlasEntry(String civOrLeaderType) {
		return iconAtlasEntriesByCivOrLeaderType.get(civOrLeaderType);
	}

}
