package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.List;
import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class LostAtSea implements ActOfGod {

	@Override
	public String getID() {
		return "LOST_AT_SEA";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		for (String modifierId : List.of("TRAIT_MAORI_MANA_OCEAN", "TRAIT_MAORI_MANA_SAILING", "TRAIT_MAORI_MANA_SHIPBUILDING", "TRAIT_MAORI_EMBARKED_ABILITY")) {
			sqlBuilder.append(ST("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('<traitType>', '<modifierId>');\n",
					Map.of(
							"traitType", civAbilityTraitType,
							"modifierId", modifierId
					)));
		}

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT OR REPLACE INTO Leaders_XP2 (LeaderType, OceanStart) VALUES ('LEADER_IMP_<modName>', 1);\n",
				Map.of("modName", modName)
		));
	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {

	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
