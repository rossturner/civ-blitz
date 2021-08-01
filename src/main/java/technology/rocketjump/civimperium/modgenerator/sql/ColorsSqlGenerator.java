package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

@Component
public class ColorsSqlGenerator {

	public String getColorsSql(ModHeader modHeader, String leaderType) {
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
				"SELECT 'LEADER_IMP_").append(modHeader.modName.toUpperCase()).append("',\n" +
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

}
