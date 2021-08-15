package technology.rocketjump.civimperium.model;

import technology.rocketjump.civimperium.codegen.tables.pojos.MatchSignup;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;

public class MatchSignupWithPlayer extends MatchSignup {

	private final Player player;

	public MatchSignupWithPlayer(MatchSignup matchSignup, Player player) {
		super(matchSignup);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public String getCard(CardCategory category) {
		switch (category) {
			case CivilizationAbility:
				return getCardCivAbility();
			case LeaderAbility:
				return getCardLeaderAbility();
			case UniqueUnit:
				return getCardUniqueUnit();
			case UniqueInfrastructure:
				return getCardUniqueInfrastruture();
			default:
				throw new IllegalArgumentException("Unrecognised card category " + category);
		}
	}

	public void setCard(Card card) {
		switch (card.getCardCategory()) {
			case CivilizationAbility:
				setCardCivAbility(card.getTraitType());
				break;
			case LeaderAbility:
				setCardLeaderAbility(card.getTraitType());
				break;
			case UniqueUnit:
				setCardUniqueUnit(card.getTraitType());
				break;
			case UniqueInfrastructure:
				setCardUniqueInfrastruture(card.getTraitType());
				break;
			default:
				throw new IllegalArgumentException("Unrecognised card category " + card.getCardCategory());
		}
	}

	public void removeCard(Card card) {
		switch (card.getCardCategory()) {
			case CivilizationAbility:
				setCardCivAbility(null);
				break;
			case LeaderAbility:
				setCardLeaderAbility(null);
				break;
			case UniqueUnit:
				setCardUniqueUnit(null);
				break;
			case UniqueInfrastructure:
				setCardUniqueInfrastruture(null);
				break;
			default:
				throw new IllegalArgumentException("Unrecognised card category " + card.getCardCategory());
		}
	}

	public void clearAllCards() {
		setCardCivAbility(null);
		setCardLeaderAbility(null);
		setCardUniqueUnit(null);
		setCardUniqueInfrastruture(null);
	}
}
