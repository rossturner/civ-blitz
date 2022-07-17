package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class ColorsSqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getColorsSql(ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase(),
				civInfo.getCard(CardCategory.LeaderAbility).getLeaderType().orElseThrow());
	}

	public String getColorsSql(String modName, String leaderType) {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("INSERT INTO PlayerColors\n" +
				"(\tType,\n" +
				"     Usage,\n" +
				"     PrimaryColor,\n" +
				"     SecondaryColor,\n" +
				"\n" +
				"     Alt1PrimaryColor,\n" +
				"     Alt1SecondaryColor,\n" +
				"\n" +
				"     Alt2PrimaryColor,\n" +
				"     Alt2SecondaryColor,\n" +
				"\n" +
				"     Alt3PrimaryColor,\n" +
				"     Alt3SecondaryColor)\n" +
				"SELECT 'LEADER_IMP_").append(modName).append("',\n" +
				"    Usage,\n" +
				"    PrimaryColor,\n" +
				"    SecondaryColor,\n" +
				"\n" +
				"    Alt1PrimaryColor,\n" +
				"    Alt1SecondaryColor,\n" +
				"\n" +
				"    Alt2PrimaryColor,\n" +
				"    Alt2SecondaryColor,\n" +
				"\n" +
				"    Alt3PrimaryColor,\n" +
				"    Alt3SecondaryColor\n" +
				"FROM PlayerColors\n" +
				"WHERE Type = '").append(leaderType).append("';");

		return sqlBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "Colors.sql";
	}
}
