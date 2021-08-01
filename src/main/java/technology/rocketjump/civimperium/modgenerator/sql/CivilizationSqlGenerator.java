package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

@Component
public class CivilizationSqlGenerator {

	@Autowired
	public CivilizationSqlGenerator() {

	}

	public String getCivilizationSql(ModHeader modHeader, Card civAbilityCard, Card leaderAbilityCard, String startBiasCivType) {
		return getCivilizationSql(
				modHeader.modName,
				leaderAbilityCard.getLeaderType().get(),
				leaderAbilityCard.getCivilizationType(),
				civAbilityCard.getCivilizationType(),
				startBiasCivType
		);
	}

	private String getCivilizationSql(String modName, String leaderType, String leaderCivType, String namesCivType, String startBiasCivType) {
		modName = modName.toUpperCase();

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("INSERT OR REPLACE INTO Types (Type, Kind)\n" +
				"VALUES\t('CIVILIZATION_IMP_").append(modName).append("',\t'KIND_CIVILIZATION');\n" +
				"\n" +
				"INSERT OR REPLACE INTO CivilizationLeaders\n" +
				"(CivilizationType, LeaderType, CapitalName)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', 'LEADER_IMP_").append(modName).append("', CivilizationLeaders.CapitalName\n" +
				"FROM CivilizationLeaders\n" +
				"WHERE CivilizationLeaders.LeaderType = '").append(leaderType).append("'\n" +
				"  and CivilizationLeaders.CivilizationType = '").append(leaderCivType).append("';\n\n");
		
		sqlBuilder.append("INSERT OR REPLACE INTO Civilizations\n" +
				"(CivilizationType, Name, Description, Adjective, StartingCivilizationLevelType, RandomCityNameDepth, Ethnicity)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', Civilizations.Name, Civilizations.Description, Civilizations.Adjective,\n" +
				"       Civilizations.StartingCivilizationLevelType,  Civilizations.RandomCityNameDepth, Civilizations.Ethnicity\n" +
				"FROM Civilizations\n" +
				"WHERE Civilizations.CivilizationType = '").append(namesCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO CityNames (CivilizationType, CityName)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', CityNames.CityName\n" +
				"from CityNames\n" +
				"where CityNames.CivilizationType = '").append(namesCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO CivilizationCitizenNames\n" +
				"(CivilizationType, CitizenName,\tFemale, Modern)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', CivilizationCitizenNames.CitizenName, CivilizationCitizenNames.Female, CivilizationCitizenNames.Modern\n" +
				"FROM CivilizationCitizenNames\n" +
				"WHERE CivilizationCitizenNames.CivilizationType = '").append(namesCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO CivilizationInfo\n" +
				"(CivilizationType, Header, Caption, SortIndex)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', CivilizationInfo.Header, CivilizationInfo.Caption, CivilizationInfo.SortIndex\n" +
				"FROM CivilizationInfo\n" +
				"WHERE CivilizationInfo.CivilizationType = '").append(namesCivType).append("';\n\n");

		sqlBuilder.append("INSERT OR REPLACE INTO StartBiasFeatures\n" +
				"(CivilizationType, FeatureType, Tier)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', StartBiasFeatures.FeatureType, StartBiasFeatures.Tier\n" +
				"FROM StartBiasFeatures\n" +
				"WHERE StartBiasFeatures.CivilizationType = '").append(startBiasCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO StartBiasResources\n" +
				"(CivilizationType, ResourceType, Tier)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', StartBiasResources.ResourceType, StartBiasResources.Tier\n" +
				"FROM StartBiasResources\n" +
				"WHERE StartBiasResources.CivilizationType = '").append(startBiasCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO StartBiasRivers\n" +
				"(CivilizationType, Tier)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', StartBiasRivers.Tier\n" +
				"FROM StartBiasRivers\n" +
				"WHERE StartBiasRivers.CivilizationType = '").append(startBiasCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO StartBiasTerrains\n" +
				"(CivilizationType, TerrainType, Tier)\n" +
				"SELECT 'CIVILIZATION_IMP_").append(modName).append("', StartBiasTerrains.TerrainType, StartBiasTerrains.Tier\n" +
				"FROM StartBiasTerrains\n" +
				"WHERE StartBiasTerrains.CivilizationType = '").append(startBiasCivType).append("';\n");

		return sqlBuilder.toString();
	}

}
