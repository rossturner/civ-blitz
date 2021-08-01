package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.IconAtlasEntry;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;

@Component
public class IconsSqlGenerator implements ImperiumFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public IconsSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, Map<CardCategory, Card> selectedCards) {
		return getIconsSql(modHeader, selectedCards);
	}

	public String getIconsSql(ModHeader modHeader, Map<CardCategory, Card> selectedCards) {
		StringBuilder sqlBuilder = new StringBuilder();

		String name = modHeader.modName.toUpperCase();

		IconAtlasEntry civIconEntry = sourceDataRepo.getIconAtlasEntry(selectedCards.get(CardCategory.CivilizationAbility).getCivilizationType());
		IconAtlasEntry leaderIconEntry = sourceDataRepo.getIconAtlasEntry(selectedCards.get(CardCategory.LeaderAbility).getLeaderType().get());

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
