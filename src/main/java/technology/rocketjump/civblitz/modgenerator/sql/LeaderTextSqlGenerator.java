package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class LeaderTextSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card civCard = civInfo.selectedCards.get(CardCategory.CivilizationAbility);
		Card leaderCard = civInfo.selectedCards.get(CardCategory.LeaderAbility);

		sqlBuilder.append("INSERT OR REPLACE INTO LocalizedText\n" +
				"(Tag, Language,\tText)\n" +
				"VALUES\n" +
				"('LOC_LEADER_IMP_").append(modName).append("', 'en_US', '").append(leaderCard.getCardName()).append(" (").append(civCard.getCardName()).append(")');");

		return sqlBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "LeaderText.sql";
	}
}
