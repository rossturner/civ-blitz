package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class Kutm implements ActOfGod {
	@Override
	public String getID() {
		return "KUTM";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 15 WHERE ModifierId = 'BASTIONS_OUTERDEFENSE' and Name = 'Amount';\n");
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 10 WHERE ModifierId = 'BASTIONS_RANGEDSTRIKE' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_BASTIONS_NAME", "King under the Mountain", sqlBuilder);
		addTranslation("LOC_POLICY_BASTIONS_DESCRIPTION", "+15 City [ICON_Strength] Defense Strength. +10 City [ICON_Ranged] Ranged Strength.", sqlBuilder);
	}

}
