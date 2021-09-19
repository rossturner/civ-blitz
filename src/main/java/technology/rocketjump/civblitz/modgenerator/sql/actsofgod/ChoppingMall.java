package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class ChoppingMall implements ActOfGod {

	@Override
	public String getID() {
		return "CHOPPING_MALL";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId) VALUES ('<traitType>', '<modifierId>');\n",
				Map.of(
						"traitType", civAbilityTraitType,
						"modifierId", "TRAIT_BUILDER_WONDER_PERCENT"
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
