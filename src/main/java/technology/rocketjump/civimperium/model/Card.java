package technology.rocketjump.civimperium.model;

import java.util.Optional;

public class Card {

	private String cardName;
	private String cardDescription;
	private String traitType; // Use this as the unique identifier
	private String civilizationType;
	private Optional<String> leaderType = Optional.empty();
	private CardCategory cardCategory;

	private String civilizationFriendlyName;

	private Optional<String> grantsTraitType = Optional.empty();
	private String subtype;

	public String getCivilizationType() {
		return civilizationType;
	}

	public void setCivilizationType(String civilizationType) {
		this.civilizationType = civilizationType;
	}

	public Optional<String> getLeaderType() {
		return leaderType;
	}

	public void setLeaderType(Optional<String> leaderType) {
		this.leaderType = leaderType;
	}

	public String getTraitType() {
		return traitType;
	}

	public void setTraitType(String traitType) {
		this.traitType = traitType;
	}

	public CardCategory getCardCategory() {
		return cardCategory;
	}

	public void setCardCategory(CardCategory cardCategory) {
		this.cardCategory = cardCategory;
	}

	public String getCivilizationFriendlyName() {
		return civilizationFriendlyName;
	}

	public void setCivilizationFriendlyName(String civilizationFriendlyName) {
		this.civilizationFriendlyName = civilizationFriendlyName;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardDescription() {
		return cardDescription;
	}

	public void setCardDescription(String cardDescription) {
		this.cardDescription = cardDescription;
	}

	public Optional<String> getGrantsTraitType() {
		return grantsTraitType;
	}

	public void setGrantsTraitType(Optional<String> grantsTraitType) {
		this.grantsTraitType = grantsTraitType;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getSubtype() {
		return subtype;
	}
}
