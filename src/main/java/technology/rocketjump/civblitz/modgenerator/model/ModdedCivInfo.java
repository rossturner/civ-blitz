package technology.rocketjump.civblitz.modgenerator.model;

import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;

import java.util.List;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;

public class ModdedCivInfo {

	public final List<Card> selectedCards;
	public final String startBiasCivType;

	public ModdedCivInfo(List<Card> selectedCards, String startBiasCivType) {
		this.selectedCards = selectedCards;
		this.startBiasCivType = startBiasCivType != null ? startBiasCivType : selectedCards.stream()
				.filter(c -> c.getCardCategory().equals(CivilizationAbility))
				.map(Card::getCivilizationType)
				.findAny().orElse("None");
	}

	public Card getCard(CardCategory cardCategory) {
		return selectedCards.stream().filter(c -> c.getCardCategory().equals(cardCategory)).findFirst().orElseThrow();
	}

}
