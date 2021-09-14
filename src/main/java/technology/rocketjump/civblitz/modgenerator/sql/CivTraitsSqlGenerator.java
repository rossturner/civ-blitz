package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

@Component
public class CivTraitsSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();
		StringBuilder sqlBuilder = new StringBuilder();

		for (CardCategory cardCategory : CardCategory.values()) {
			if (!cardCategory.equals(CardCategory.LeaderAbility)) {
				Card card = civInfo.selectedCards.get(cardCategory);
				addLine(sqlBuilder, card.getTraitType(), modName);
				if (card.getGrantsTraitType().isPresent()) {
					addLine(sqlBuilder, card.getGrantsTraitType().get(), modName);
				}

				if (cardCategory.equals(CardCategory.CivilizationAbility)) {
					for (ActOfGod actOfGod : modHeader.actsOfGod) {
						actOfGod.applyToCivTrait(card.getTraitType(), modName, sqlBuilder);
					}
				}
			}
		}

		Card civAbilityCard = civInfo.selectedCards.get(CardCategory.CivilizationAbility);
		if (civAbilityCard.getTraitType().equals("TRAIT_CIVILIZATION_MAYAB")) {
			sqlBuilder.append("INSERT OR REPLACE INTO RequirementArguments (RequirementId, Name, Type, Value) VALUES (")
					.append("'PLAYER_IS_MAYA', 'CivilizationType', 'ARGTYPE_IDENTITY', '").append(modName).append("');\n");
		}

		return sqlBuilder.toString();
	}

	private void addLine(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('")
				.append(traitType).append("', 'CIVILIZATION_IMP_").append(modName).append("');\n");
	}

	@Override
	public String getFilename() {
		return "CivTraits.sql";
	}
}
