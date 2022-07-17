package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.IconAtlasEntry;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class IconsSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public IconsSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String name = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		IconAtlasEntry civIconEntry = sourceDataRepo.getIconAtlasEntry(civInfo.getCard(CardCategory.CivilizationAbility).getCivilizationType());
		IconAtlasEntry leaderIconEntry = sourceDataRepo.getIconAtlasEntry(civInfo.getCard(CardCategory.LeaderAbility).getLeaderType().orElseThrow());

		sqlBuilder.append("INSERT OR REPLACE INTO IconDefinitions\n" +
				"\t\t(Name,\t\t\t\t\t\t\t\t\tAtlas, \t\t\t\t\t\t\t\t\t'Index')\n" +
				"VALUES\t('ICON_CIVILIZATION_IMP_").append(name).append("',\t\t'").append(civIconEntry.atlasName).append("',\t\t\t").append(civIconEntry.index).append("),\n" +
				"\t\t('ICON_LEADER_IMP_").append(name).append("',\t\t\t'").append(leaderIconEntry.atlasName).append("',\t\t\t\t").append(leaderIconEntry.index).append(");\n");

		return sqlBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "Icons.sql";
	}
}
