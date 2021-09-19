package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class SettleMettle implements ActOfGod {
	@Override
	public String getID() {
		return "SETTLE_METTLE";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE Units SET BaseMoves = 4 WHERE UnitType = 'UNIT_SETTLER';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
