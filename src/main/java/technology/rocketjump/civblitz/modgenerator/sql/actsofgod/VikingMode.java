package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.List;
import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class VikingMode implements ActOfGod {

	@Override
	public String getID() {
		return "VIKING_MODE";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		for (String modifierId : List.of("TRAIT_GRANT_COASTAL_RAID_ABILITY", "TRAIT_LEADER_PILLAGE_SCIENCE_MINES",
				"TRAIT_LEADER_PILLAGE_CULTURE_QUARRIES", "TRAIT_LEADER_PILLAGE_CULTURE_PLANTATIONS", "TRAIT_LEADER_PILLAGE_CULTURE_PASTURES", "TRAIT_LEADER_PILLAGE_CULTURE_CAMPS")) {
			sqlBuilder.append(ST("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('<traitType>', '<modifierId>');\n",
					Map.of(
							"traitType", civAbilityTraitType,
							"modifierId", modifierId
					)));
		}
	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT INTO LeaderTraits (LeaderType, TraitType) VALUES ('LEADER_IMP_<modName>', '<traitType>');\n",
				Map.of(
						"modName", modName,
						"traitType", "TRAIT_LEADER_UNIT_NORWEGIAN_LONGSHIP"
				)));
	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {

	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
