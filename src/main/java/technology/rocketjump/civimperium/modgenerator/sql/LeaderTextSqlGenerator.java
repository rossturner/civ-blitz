package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.model.ModdedCivInfo;

@Component
public class LeaderTextSqlGenerator implements ImperiumFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = modHeader.modName.toUpperCase();

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
