package technology.rocketjump.civblitz.model;

import java.util.*;

public class Card {

	private static final int MAX_CARD_ID_LENGTH = 60;

	public static final List<String> BANNED_CARDS = Arrays.asList(
			"TRAIT_CIVILIZATION_BYZANTIUM",
			"TRAIT_CIVILIZATION_BABYLON",
			"TRAIT_CIVILIZATION_UNIT_AMERICAN_P51",
			"TRAIT_CIVILIZATION_UNIT_GERMAN_UBOAT"
	);

	protected String identifier;
	protected String baseCardName;
	protected String baseCardDescription;
	protected String enhancedCardName;
	protected String enhancedCardDescription;
	protected String traitType;
	protected String civilizationType;
	protected Optional<String> leaderType = Optional.empty();
	protected CardCategory cardCategory;
	protected SuperCategory superCategory = SuperCategory.Standard;
	protected CardRarity rarity = CardRarity.Common;

	protected String civilizationFriendlyName;

	protected Optional<String> grantsTraitType = Optional.empty();
	protected Optional<String> grantsFreeUseOfCard = Optional.empty();
	protected String subtype;
	protected String mediaName;
	protected String requiredDlc;
	protected List<String> modifierIds = new ArrayList<>();
	protected String gameplaySQL;
	protected String localisationSQL;

	public Card() {

	}

	public Card(Card original) {
		this.identifier = original.identifier;
		this.baseCardName = original.baseCardName;
		this.baseCardDescription = original.baseCardDescription;
		this.traitType = original.traitType;
		this.civilizationType = original.civilizationType;
		this.leaderType = original.leaderType;
		this.cardCategory = original.cardCategory;
		this.civilizationFriendlyName = original.civilizationFriendlyName;
		this.grantsTraitType = original.grantsTraitType;
		this.grantsFreeUseOfCard = original.grantsFreeUseOfCard;
		this.subtype = original.subtype;
		this.mediaName = original.mediaName;
		this.requiredDlc = original.requiredDlc;
		this.modifierIds.addAll(original.modifierIds);
		this.superCategory = original.superCategory;
		this.rarity = original.rarity;
		this.gameplaySQL = original.gameplaySQL;
		this.localisationSQL = original.localisationSQL;
	}

	@Override
	public String toString() {
		return baseCardName + " " + traitType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Card card = (Card) o;
		return traitType.equals(card.traitType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(traitType);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		if (identifier.length() > MAX_CARD_ID_LENGTH) {
			this.identifier = identifier.substring(0, MAX_CARD_ID_LENGTH);
		} else {
			this.identifier = identifier;
		}
	}

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

	public String getBaseCardName() {
		return baseCardName;
	}

	public void setBaseCardName(String baseCardName) {
		this.baseCardName = baseCardName;
	}

	public String getBaseCardDescription() {
		return baseCardDescription;
	}

	public void setBaseCardDescription(String baseCardDescription) {
		this.baseCardDescription = baseCardDescription;
	}

	public String getEnhancedCardName() {
		return enhancedCardName;
	}

	public void setEnhancedCardName(String enhancedCardName) {
		this.enhancedCardName = enhancedCardName;
	}

	public String getEnhancedCardDescription() {
		return enhancedCardDescription;
	}

	public void setEnhancedCardDescription(String enhancedCardDescription) {
		this.enhancedCardDescription = enhancedCardDescription;
	}

	public Optional<String> getGrantsTraitType() {
		return grantsTraitType;
	}

	public void setGrantsTraitType(Optional<String> grantsTraitType) {
		this.grantsTraitType = grantsTraitType;
	}

	public Optional<String> getGrantsFreeUseOfCard() {
		return grantsFreeUseOfCard;
	}

	public void setGrantsFreeUseOfCard(Optional<String> grantsFreeUseOfCard) {
		this.grantsFreeUseOfCard = grantsFreeUseOfCard;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getRequiredDlc() {
		return requiredDlc;
	}

	public void setRequiredDlc(String requiredDlc) {
		this.requiredDlc = requiredDlc;
	}

	public List<String> getModifierIds() {
		return modifierIds;
	}

	public SuperCategory getSuperCategory() {
		return superCategory;
	}

	public void setSuperCategory(SuperCategory superCategory) {
		this.superCategory = superCategory;
	}

	public CardRarity getRarity() {
		return rarity;
	}

	public void setRarity(CardRarity rarity) {
		this.rarity = rarity;
	}

	public String getGameplaySQL() {
		return gameplaySQL;
	}

	public void setGameplaySQL(String gameplaySQL) {
		this.gameplaySQL = gameplaySQL;
	}

	public String getLocalisationSQL() {
		return localisationSQL;
	}

	public void setLocalisationSQL(String localisationSQL) {
		this.localisationSQL = localisationSQL;
	}
}
