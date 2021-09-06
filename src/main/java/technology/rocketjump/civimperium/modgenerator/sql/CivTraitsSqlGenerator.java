package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.model.ModdedCivInfo;

@Component
public class CivTraitsSqlGenerator extends ImperiumFileGenerator {

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
