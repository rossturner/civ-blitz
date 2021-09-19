package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Contractors.addTraitModifier;

public class ChoppingMall implements ActOfGod {

	@Override
	public String getID() {
		return "CHOPPING_MALL";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		addTraitModifier("TRAIT_BUILDER_WONDER_PERCENT", civAbilityTraitType, sqlBuilder);
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
