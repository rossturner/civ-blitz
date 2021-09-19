package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class GodImmortal implements ActOfGod {
	@Override
	public String getID() {
		return "GOD_IMMORTAL";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 5 WHERE ModifierId = 'GOD_KING_GOLD' and Name = 'Amount';\n");
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 5 WHERE ModifierId = 'GOD_KING_FAITH' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_GOD_KING_NAME", "God Immortal", sqlBuilder);
		addTranslation("LOC_POLICY_GOD_KING_DESCRIPTION", "+5 [ICON_Faith] Faith and +5 [ICON_Gold] Gold in the [ICON_Capital] Capital.", sqlBuilder);
	}

}
