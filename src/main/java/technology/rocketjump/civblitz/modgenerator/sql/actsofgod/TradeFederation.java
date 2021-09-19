package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class TradeFederation implements ActOfGod {
	@Override
	public String getID() {
		return "TRADE_FEDERATION";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 2 WHERE ModifierId = 'MERCHANTCONFEDERATION_INFLUENCETOKENGOLD' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_MERCHANT_CONFEDERATION_NAME", "Trade Federation", sqlBuilder);
		addTranslation("LOC_POLICY_MERCHANT_CONFEDERATION_DESCRIPTION", "+2 [ICON_Gold] Gold from each of your [ICON_Envoy] Envoys at city-states.", sqlBuilder);
	}

}
