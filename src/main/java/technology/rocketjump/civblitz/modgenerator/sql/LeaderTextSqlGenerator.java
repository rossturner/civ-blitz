package technology.rocketjump.civblitz.modgenerator.sql;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.List;

@Component
public class LeaderTextSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card civCard = civInfo.getCard(CardCategory.CivilizationAbility);
		Card leaderCard = civInfo.getCard(CardCategory.LeaderAbility);

		sqlBuilder.append("INSERT OR REPLACE INTO LocalizedText\n" +
				"(Tag, Language,\tText)\n" +
				"VALUES\n" +
				"('LOC_LEADER_IMP_").append(modName).append("', 'en_US', '").append(leaderCard.getBaseCardName()).append(" (").append(civCard.getBaseCardName()).append(")');");

		for (Card card : civInfo.selectedCards) {
			if (!StringUtils.isEmpty(card.getLocalisationSQL())) {
				sqlBuilder.append("\n\n-- ").append(card.getIdentifier()).append("\n").append(card.getLocalisationSQL());
			}
		}

		return sqlBuilder.toString();
	}

	@Override
	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getFileContents(modHeader, civs));
		for (ActOfGod actOfGod : modHeader.actsOfGod) {
			actOfGod.applyLocalisationChanges(builder);
		}
		return builder.toString();
	}

	@Override
	public String getFilename() {
		return "LeaderText.sql";
	}
}
