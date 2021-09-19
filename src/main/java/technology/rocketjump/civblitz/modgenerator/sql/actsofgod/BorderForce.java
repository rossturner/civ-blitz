package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Contractors.addTraitModifier;

public class BorderForce implements ActOfGod {

	@Override
	public String getID() {
		return "BORDER_FORCE";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		addTraitModifier("TRAIT_INCREASED_TILES", civAbilityTraitType, sqlBuilder);
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
