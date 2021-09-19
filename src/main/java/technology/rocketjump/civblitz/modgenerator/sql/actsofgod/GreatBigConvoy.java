package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class GreatBigConvoy implements ActOfGod {
	@Override
	public String getID() {
		return "GREAT_BIG_CONVOY";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 5 WHERE ModifierId = 'CARAVANSARIES_TRADEROUTEGOLD' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_CARAVANSARIES_NAME", "Great Big Convoy", sqlBuilder);
		addTranslation("LOC_POLICY_CARAVANSARIES_DESCRIPTION", "+5 [ICON_Gold] Gold from all [ICON_TradeRoute] Trade Routes.", sqlBuilder);
	}

}
