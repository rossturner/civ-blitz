package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class GodEmperor implements ActOfGod {
	@Override
	public String getID() {
		return "GOD_EMPEROR";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 2 WHERE ModifierId = 'GOD_KING_GOLD' and Name = 'Amount';\n");
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 2 WHERE ModifierId = 'GOD_KING_FAITH' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_GOD_KING_NAME", "God King", sqlBuilder);
		addTranslation("LOC_POLICY_GOD_KING_DESCRIPTION", "+2 [ICON_Faith] Faith and +2 [ICON_Gold] Gold in the [ICON_Capital] Capital.", sqlBuilder);
	}

}
