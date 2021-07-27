package technology.rocketjump.civimperium.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class SourceDataRepo {

	private final Map<String, String> friendlyNameByCivilizationType = new HashMap<>();
	private final Map<String, Card> cardsByTraitType = new HashMap<>();

	@Autowired
	public SourceDataRepo() {

	}

	public void addCivFriendlyName(String civilizationType, String civFriendlyName) {
		friendlyNameByCivilizationType.put(civilizationType, civFriendlyName);
	}

	public void add(Card card) {
		cardsByTraitType.put(card.getTraitType(), card);
	}

	public void removeGrantedCards() {
		for (String traitType : new ArrayList<>(cardsByTraitType.keySet())) {
			Card card = cardsByTraitType.get(traitType);
			if (card != null && card.getGrantsTraitType().isPresent()) {
				cardsByTraitType.remove(card.getGrantsTraitType().get());
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
