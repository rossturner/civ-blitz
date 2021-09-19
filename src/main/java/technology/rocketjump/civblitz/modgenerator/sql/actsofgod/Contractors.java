package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class Contractors implements ActOfGod {

	@Override
	public String getID() {
		return "CONTRACTORS";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		addTraitModifier("TRAIT_LEADER_NEARBY_CITIES_GAIN_BUILDER", civAbilityTraitType, sqlBuilder);
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

	public static void addTraitModifier(String modifierId, String civAbilityTraitType, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('<traitType>', '<modifierId>');\n",
				Map.of(
						"traitType", civAbilityTraitType,
						"modifierId", modifierId
				)));
	}
}
