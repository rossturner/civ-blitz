package technology.rocketjump.civblitz.model;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SourceDataRepo {

	private final Map<String, String> friendlyNameByCivilizationType = new TreeMap<>();

	private final Map<String, Card> cardsByIdentifier = new TreeMap<>();
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
	public final Map<String, Card> civAbilityCardByFriendlyName = new HashMap<>();

	@Autowired
	public SourceDataRepo() {

	}

	public void addCivFriendlyName(String civilizationType, String civFriendlyName) {
		friendlyNameByCivilizationType.put(civilizationType, civFriendlyName);
	}

	public void add(Card card) {
		if (cardsByIdentifier.containsKey(card.getIdentifier())) {
			System.out.println("Duplicate card for identifier " + card.getIdentifier());
		}

		cardsByIdentifier.put(card.getIdentifier(), card);
		cardsByCategory.computeIfAbsent(card.getCardCategory(), a -> new ArrayList<>()).add(card);
	}

	public void removeGrantedCards() {
		for (String identifier : new ArrayList<>(cardsByIdentifier.keySet())) {
			Card card = cardsByIdentifier.get(identifier);
			if (card != null && card.getGrantsTraitType().isPresent()) {
				Card removed = cardsByIdentifier.remove("COMMON_"+card.getGrantsTraitType().get());
				cardsByCategory.get(removed.getCardCategory()).remove(removed);
			}
		}

	}

	public Collection<Card> getAll() {
		return cardsByIdentifier.values();
	}

	public String getFriendlyCivName(String civilizationType) {
		return friendlyNameByCivilizationType.get(civilizationType);
	}

	public Card getBaseCardByTraitType(String traitType) {
		return cardsByIdentifier.get("COMMON_" + traitType);
	}

	public Card getByIdentifier(String cardIdentifier) {
		return cardsByIdentifier.get(cardIdentifier);
	}

	public List<Card> getByCategory(CardCategory cardCategory, Optional<CardRarity> optionalRarity) {
		if (optionalRarity.isPresent()) {
			return cardsByCategory.get(cardCategory).stream()
					.filter(c -> c.getRarity().equals(optionalRarity.get()))
					.collect(Collectors.toList());
		} else {
			return cardsByCategory.get(cardCategory);
		}
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

	public void linkCivAbilityCard(String civilizationFriendlyName, Card card) {
		civAbilityCardByFriendlyName.put(civilizationFriendlyName, card);
	}
}
