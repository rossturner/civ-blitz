package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Contractors.addTraitModifier;

public class MistOfWar implements ActOfGod {

	@Override
	public String getID() {
		return "MIST_OF_WAR";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		addTraitModifier("TRAIT_JOAO_EXTRA_SIGHT", civAbilityTraitType, sqlBuilder);
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
