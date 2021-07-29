package technology.rocketjump.civimperium.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SourceDataRepo {

	private final Map<String, String> friendlyNameByCivilizationType = new TreeMap<>();
	private final Map<String, Card> cardsByTraitType = new TreeMap<>();
	private final Map<String, Card> cardsByCardName = new TreeMap<>();
	private final Set<String> traitsGrantedByOthers = new HashSet<>();

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
	}

	public void removeGrantedCards() {
		for (String traitType : new ArrayList<>(cardsByTraitType.keySet())) {
			Card card = cardsByTraitType.get(traitType);
			if (card != null && card.getGrantsTraitType().isPresent()) {
				cardsByTraitType.remove(card.getGrantsTraitType().get());
				cardsByCardName.remove(card.getCardName());
			}
		}

	}

	public Collection<Card> getAll() {
		return cardsByTraitType.values();
	}

	public String getFriendlyCivName(String civilizationType) {
		return friendlyNameByCivilizationType.get(civilizationType);
	}
}