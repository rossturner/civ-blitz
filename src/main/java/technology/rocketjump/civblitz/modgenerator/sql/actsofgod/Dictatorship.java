package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class Dictatorship implements ActOfGod {
	@Override
	public String getID() {
		return "DICTATORSHIP";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 5 WHERE ModifierId = 'PRAETORIUM_GOVERNORIDENTITY' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_PRAETORIUM_NAME", "Dictatorship", sqlBuilder);
		addTranslation("LOC_POLICY_PRAETORIUM_DESCRIPTION", "Governors provide +5 Loyalty per turn to their city.", sqlBuilder);
	}

}
