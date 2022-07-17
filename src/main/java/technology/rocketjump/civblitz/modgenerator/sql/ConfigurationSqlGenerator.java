package technology.rocketjump.civblitz.modgenerator.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

@Component
public class ConfigurationSqlGenerator extends BlitzFileGenerator {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public ConfigurationSqlGenerator(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@Override
	public String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo) {
		StringBuilder sqlBuilder = new StringBuilder();

		String modName = ModHeaderGenerator.buildName(civInfo.selectedCards).toUpperCase();

		Card civAbilityCard = civInfo.getCard(CardCategory.CivilizationAbility);
		String civType = civAbilityCard.getCivilizationType();
		String civName = sourceDataRepo.civNameByCivType.get(civType);
		String civIcon = sourceDataRepo.civIconByCivType.get(civType);

		String civAbilityName = sourceDataRepo.civAbilityNameByCivType.get(civType);
		String civAbilityDesc = sourceDataRepo.civAbilityDescByCivType.get(civType);
		String civAbilityIcon = sourceDataRepo.civAbilityIconByCivType.get(civType);

		Card leaderAbilityCard = civInfo.getCard(CardCategory.LeaderAbility);
		String leaderType = leaderAbilityCard.getLeaderType().get();
		String leaderTrait = leaderAbilityCard.getTraitType();
		String leaderIcon = sourceDataRepo.leaderIconByLeaderType.get(leaderType);
		String leaderAbilityIcon = sourceDataRepo.leaderAbilityIconByLeaderType.get(leaderType);

		String locLeaderTraitName = sourceDataRepo.leaderTraitNameByTraitType.getOrDefault(leaderTrait, "");
		String locLeaderTraitDesc = sourceDataRepo.leaderTraitDescByTraitType.getOrDefault(leaderTrait, "");

		String portrait = sourceDataRepo.portraitsByLeaderType.getOrDefault(leaderType, "");
		String portraitBackground = sourceDataRepo.portraitBackgroundsByLeaderType.getOrDefault(leaderType,"");

		sqlBuilder.append("INSERT OR REPLACE INTO Players\n" +
				"(Domain,\n" +
				" CivilizationType,\n" +
				" Portrait,\n" +
				" PortraitBackground,\n" +
				" LeaderType,\n" +
				" LeaderName,\n" +
				" LeaderIcon,\n" +
				" LeaderAbilityName,\n" +
				" LeaderAbilityDescription,\n" +
				" LeaderAbilityIcon,\n" +
				" CivilizationName,\n" +
				" CivilizationIcon,\n" +
				" CivilizationAbilityName,\n" +
				" CivilizationAbilityDescription,\n" +
				" CivilizationAbilityIcon)\n" +
				"VALUES\n" +
				"('Players:Expansion2_Players',\n" +
				" 'CIVILIZATION_IMP_").append(modName).append("',\n" +
				" '").append(portrait).append("',\n" +
				" '").append(portraitBackground).append("',\n" +
				" 'LEADER_IMP_").append(modName).append("',\n" +
				" 'LOC_LEADER_IMP_").append(modName).append("',\n" +
				" '").append(leaderIcon).append("',\n" +
				" '").append(locLeaderTraitName).append("',\n" +
				" '").append(locLeaderTraitDesc).append("',\n" +
				" '").append(leaderAbilityIcon).append("',\n" +
				" '").append(civName).append("',\n" +
				" '").append(civIcon).append("',\n" +
				" '").append(civAbilityName).append("',\n" +
				" '").append(civAbilityDesc).append("',\n" +
				" '").append(civAbilityIcon).append("'\n" +
				");\n\n");

		int sortIndex = 10;

		for (Card card : civInfo.selectedCards) {
			if (CardCategory.mainCategories.contains(card.getCardCategory())) {
				if (!card.getCardCategory().equals(CardCategory.LeaderAbility) && !card.getCardCategory().equals(CardCategory.CivilizationAbility)) {
					addTraitPlayerItem(sqlBuilder, modName, card.getTraitType(), card.getCivilizationType(), sortIndex);
					sortIndex += 10;
				}

				// add granted stuff, including from CA and LA
				if (card.getGrantsTraitType().isPresent()) {
					addTraitPlayerItem(sqlBuilder, modName, card.getGrantsTraitType().get(), card.getCivilizationType(), sortIndex);
					sortIndex += 10;
				}
			}
		}

		return sqlBuilder.toString();
	}

	private void addTraitPlayerItem(StringBuilder sqlBuilder, String modName, String traitType, String civilizationType, int sortIndex) {
		String subtype = sourceDataRepo.getSubtypeByTraitType(traitType);

		sqlBuilder.append("INSERT OR REPLACE INTO PlayerItems (Domain, CivilizationType, LeaderType, Type, Icon, Name, Description, SortIndex)\n" +
				"SELECT 'Players:Expansion2_Players', 'CIVILIZATION_IMP_").append(modName)
				.append("', 'LEADER_IMP_").append(modName)
				.append("', PlayerItems.Type, PlayerItems.Icon, PlayerItems.Name, PlayerItems.Description, ").append(sortIndex).append("\n" +
				"FROM PlayerItems\n" +
				"WHERE Domain = 'Players:Expansion2_Players' and CivilizationType = '").append(civilizationType).append("' and Type = '").append(subtype).append("';\n\n");
	}

	@Override
	public String getFilename() {
		return "Configuration.sql";
	}
}
