package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class GrandmasterMonk implements ActOfGod {
	@Override
	public String getID() {
		return "GRANDMASTER_MONK";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE Units SET Combat = 60 WHERE UnitType = 'UNIT_WARRIOR_MONK';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
