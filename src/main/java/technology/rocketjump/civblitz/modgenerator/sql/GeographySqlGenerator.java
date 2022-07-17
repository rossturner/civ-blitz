package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class GeographySqlGenerator extends BlitzFileGenerator {

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		return getGeographySql(ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase(),
				civInfo.getCard(CardCategory.CivilizationAbility).getCivilizationType());
	}

	public String getGeographySql(String modName, String geographyCivType) {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("INSERT OR REPLACE INTO NamedRiverCivilizations (NamedRiverType, CivilizationType)\n" +
				"SELECT NamedRiverCivilizations.NamedRiverType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedRiverCivilizations\n" +
				"WHERE NamedRiverCivilizations.CivilizationType = '").append(geographyCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedMountainCivilizations (NamedMountainType, CivilizationType)\n" +
				"SELECT NamedMountainCivilizations.NamedMountainType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedMountainCivilizations\n" +
				"WHERE NamedMountainCivilizations.CivilizationType = '").append(geographyCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedVolcanoCivilizations (NamedVolcanoType, CivilizationType)\n" +
				"SELECT NamedVolcanoCivilizations.NamedVolcanoType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedVolcanoCivilizations\n" +
				"WHERE NamedVolcanoCivilizations.CivilizationType = '").append(geographyCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedDesertCivilizations (NamedDesertType, CivilizationType)\n" +
				"SELECT NamedDesertCivilizations.NamedDesertType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedDesertCivilizations\n" +
				"WHERE NamedDesertCivilizations.CivilizationType = '").append(geographyCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedLakeCivilizations (NamedLakeType, CivilizationType)\n" +
				"SELECT NamedLakeCivilizations.NamedLakeType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedLakeCivilizations\n" +
				"WHERE NamedLakeCivilizations.CivilizationType = '").append(geographyCivType).append("';\n" +
				"\n" +
				"INSERT OR REPLACE INTO NamedSeaCivilizations (NamedSeaType, CivilizationType)\n" +
				"SELECT NamedSeaCivilizations.NamedSeaType, 'CIVILIZATION_IMP_").append(modName.toUpperCase()).append("'\n" +
				"FROM NamedSeaCivilizations\n" +
				"WHERE NamedSeaCivilizations.CivilizationType = '").append(geographyCivType).append("';\n");

		return sqlBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "Geography.sql";
	}
}
