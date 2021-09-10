package technology.rocketjump.civblitz.modgenerator.model;

import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;

import java.util.Map;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;

public class ModdedCivInfo {

	public final Map<CardCategory, Card> selectedCards;
	public final String startBiasCivType;

	public ModdedCivInfo(Map<CardCategory, Card> selectedCards, String startBiasCivType) {
		this.selectedCards = selectedCards;
		this.startBiasCivType = startBiasCivType != null ? startBiasCivType : selectedCards.get(CivilizationAbility).getCivilizationType();
	}

}
