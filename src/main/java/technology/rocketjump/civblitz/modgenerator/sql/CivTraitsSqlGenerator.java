package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;
import static technology.rocketjump.civblitz.model.CardCategory.Power;

@Component
public class CivTraitsSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
		StringBuilder sqlBuilder = new StringBuilder();

		for (CardCategory cardCategory : CardCategory.mainCategories) {
			if (!cardCategory.equals(CardCategory.LeaderAbility)) {
				Card card = civInfo.getCard(cardCategory);
				addCivTraitLine(sqlBuilder, card.getTraitType(), modName);
				if (card.getGrantsTraitType().isPresent()) {
					addCivTraitLine(sqlBuilder, card.getGrantsTraitType().get(), modName);
				}

				if (cardCategory.equals(CivilizationAbility)) {
					for (ActOfGod actOfGod : modHeader.actsOfGod) {
						actOfGod.applyToCivTrait(card.getTraitType(), modName, sqlBuilder);
					}
				}
			}
		}

		Card civAbilityCard = civInfo.getCard(CivilizationAbility);
		if (civAbilityCard.getTraitType().equals("TRAIT_CIVILIZATION_MAYAB")) {
			sqlBuilder.append("INSERT OR REPLACE INTO RequirementArguments (RequirementId, Name, Type, Value) VALUES (")
					.append("'PLAYER_IS_MAYA', 'CivilizationType', 'ARGTYPE_IDENTITY', '").append(modName).append("');\n");
		}

		civInfo.selectedCards.stream()
				.filter(card -> card.getCardCategory().equals(Power))
				.forEach(powerCard -> {
					sqlBuilder.append("-- ").append(powerCard.getIdentifier()).append("\n");
					if (powerCard.getGrantsTraitType().isPresent()) {
						addCivTraitLine(sqlBuilder, powerCard.getGrantsTraitType().get(), modName);
					}
					powerCard.getModifierIds().forEach(modifierId -> {
						addTraitModifierLine(sqlBuilder, civAbilityCard.getTraitType(), modifierId);
					});
				});

		return sqlBuilder.toString();
	}

	private void addTraitModifierLine(StringBuilder sqlBuilder, String civAbilityTraitType, String modifierId) {
		sqlBuilder.append("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('")
				.append(civAbilityTraitType).append("', '").append(modifierId).append("');\n");
	}

	private void addCivTraitLine(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('")
				.append(traitType).append("', 'CIVILIZATION_IMP_").append(modName).append("');\n");
	}

	@Override
	public String getFilename() {
		return "CivTraits.sql";
	}
}
