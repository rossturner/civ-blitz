package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;

@Component
public class LeaderTextSqlGenerator {

	public String getLeaderTextSql(ModHeader modHeader, Map<CardCategory, Card> selectedCards) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = modHeader.modName.toUpperCase();

		Card civCard = selectedCards.get(CardCategory.CivilizationAbility);
		Card leaderCard = selectedCards.get(CardCategory.LeaderAbility);

		sqlBuilder.append("INSERT OR REPLACE INTO LocalizedText\n" +
				"(Tag, Language,\tText)\n" +
				"VALUES\n" +
				"('LOC_LEADER_IMP_").append(modName).append("', 'en_US', '").append(leaderCard.getCardName()).append(" (").append(civCard.getCardName()).append(")');");

		return sqlBuilder.toString();
	}

	private void addLeaderTrait(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT INTO LeaderTraits (LeaderType, TraitType)\n" +
				"VALUES ('LEADER_IMP_").append(modName).append("', '").append(traitType).append("');\n");
	}

}
