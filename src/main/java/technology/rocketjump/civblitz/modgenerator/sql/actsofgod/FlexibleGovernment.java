package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class FlexibleGovernment implements ActOfGod {

	@Override
	public String getID() {
		return "FLEXIBLE_GOVERNMENT";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('<traitType>', 'CIVILIZATION_IMP_<modName>');\n",
				Map.of(
						"modName", modName,
						"traitType", "TRAIT_CIVILIZATION_PLATOS_REPUBLIC"
				)));
	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {
	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {

	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
