package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.model.ModdedCivInfo;

@Component
public class LeaderSqlGenerator extends ImperiumFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card leaderCard = civInfo.selectedCards.get(CardCategory.LeaderAbility);
		String leaderType = leaderCard.getLeaderType().get();

		// TODO replace leaders insert with VALUES statement

		sqlBuilder.append("\n" +
				"INSERT INTO Types\n" +
				"(Type, Kind)\n" +
				"VALUES\t('LEADER_IMP_").append(modName).append("', 'KIND_LEADER');\n" +
				"\n" +
				"INSERT INTO Leaders\n" +
				"(LeaderType, Name, InheritFrom, SceneLayers, Sex, SameSexPercentage)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', 'LOC_LEADER_IMP_").append(modName).append("', Leaders.InheritFrom , Leaders.SceneLayers , Leaders.Sex, Leaders.SameSexPercentage\n" +
				"FROM Leaders\n" +
				"WHERE Leaders.LeaderType = '").append(leaderType).append("';\n" +
				"\n" +
				"INSERT INTO DuplicateLeaders\n" +
				"(LeaderType, OtherLeaderType)\n" +
				"VALUES ('").append(leaderType).append("', 'LEADER_IMP_").append(modName).append("');\n" +
				"\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- DiplomacyInfo\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"INSERT INTO DiplomacyInfo (Type, BackgroundImage)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', DiplomacyInfo.BackgroundImage\n" +
				"FROM DiplomacyInfo\n" +
				"WHERE DiplomacyInfo.Type = '").append(leaderType).append("';\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- LeaderTraits\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n");

		addLeaderTrait(sqlBuilder, leaderCard.getTraitType(), modName);

		if (leaderCard.getGrantsTraitType().isPresent()) {
			addLeaderTrait(sqlBuilder, leaderCard.getGrantsTraitType().get(), modName);
		}

		sqlBuilder.append(
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- LeaderQuotes\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"INSERT INTO LeaderQuotes (LeaderType, Quote)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', Quote\n" +
				"FROM LeaderQuotes\n" +
				"WHERE LeaderType = '").append(leaderType).append("';\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- HistoricalAgendas\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"INSERT INTO HistoricalAgendas (LeaderType, AgendaType)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', AgendaType\n" +
				"FROM HistoricalAgendas\n" +
				"WHERE LeaderType = '").append(leaderType).append("';\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- AgendaPreferredLeaders\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"INSERT INTO AgendaPreferredLeaders (LeaderType, AgendaType)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', AgendaType\n" +
				"FROM AgendaPreferredLeaders\n" +
				"WHERE LeaderType = '").append(leaderType).append("';\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"-- FavoredReligions\n" +
				"--------------------------------------------------------------------------------------------------------------------------\n" +
				"INSERT OR REPLACE INTO FavoredReligions (LeaderType, ReligionType)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', ReligionType\n" +
				"FROM FavoredReligions\n" +
				"WHERE LeaderType = '").append(leaderType).append("';\n" +
				"--==========================================================================================================================\n" +
				"-- LEADERS: LOADING INFO\n" +
				"--==========================================================================================================================\n" +
				"INSERT INTO LoadingInfo (LeaderType, BackgroundImage, ForegroundImage, PlayDawnOfManAudio)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("', BackgroundImage, ForegroundImage, PlayDawnOfManAudio\n" +
				"FROM LoadingInfo\n" +
				"WHERE LeaderType = '").append(leaderType).append("';");

		return sqlBuilder.toString();
	}

	private void addLeaderTrait(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT INTO LeaderTraits (LeaderType, TraitType)\n" +
				"VALUES ('LEADER_IMP_").append(modName).append("', '").append(traitType).append("');\n");
	}

	@Override
	public String getFilename() {
		return "Leader.sql";
	}
}
