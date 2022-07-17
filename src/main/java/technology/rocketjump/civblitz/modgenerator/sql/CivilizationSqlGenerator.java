package technology.rocketjump.civblitz.modgenerator.sql;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import static technology.rocketjump.civblitz.model.CardCategory.CivilizationAbility;

@Component
public class CivilizationSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CivilizationSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		Card civAbilityCard = civInfo.getCard(CivilizationAbility);
		return getCivilizationSql(
				ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase(),
				civAbilityCard.getCivilizationType(),
				civInfo.startBiasCivType
		);
	}

	private String getCivilizationSql(String modName, String namesCivType, String startBiasCivType) {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("INSERT OR REPLACE INTO Types (Type, Kind)\n" +
				"VALUES\t('CIVILIZATION_IMP_").append(modName).append("',\t'KIND_CIVILIZATION');\n" +
				"\n");

		CSVRecord civRecord = sourceDataRepo.civilizationCsvRecordsByCivType.get(namesCivType);
		String ethnicity = civRecord.get("Ethnicity");
		if (ethnicity == null) {
			ethnicity = "";
		}

		sqlBuilder.append("INSERT OR REPLACE INTO Civilizations\n" +
				"(CivilizationType, Name, Description, Adjective, StartingCivilizationLevelType, RandomCityNameDepth, Ethnicity)\n" +
				"VALUES\n" +
				"('CIVILIZATION_IMP_").append(modName).append("', " +
				"'").append(civRecord.get("Name")).append("', '").append(civRecord.get("Description")).append("', '").append(civRecord.get("Adjective")).append("', " +
				"'CIVILIZATION_LEVEL_FULL_CIV', 10, '").append(ethnicity).append("');\n\n");

		// TODO replace CivLeaders insert with a VALUES statement

		String capitalName = sourceDataRepo.capitalNamesByCivType.get(namesCivType);

		sqlBuilder.append("\n" +
				"INSERT OR REPLACE INTO CivilizationLeaders\n" +
				"(CivilizationType, LeaderType, CapitalName)\n" +
				"VALUES ('CIVILIZATION_IMP_").append(modName).append("', 'LEADER_IMP_").append(modName).append("', '").append(capitalName).append("');\n\n");

		// Names aren't as important, see how fallback works

		sqlBuilder.append("\n" +
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

		// TODO collate start biases and replace with VALUES insert

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

	@Override
	public String getFilename() {
		return "Civilization.sql";
	}
}
