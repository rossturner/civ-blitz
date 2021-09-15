package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class WoodenSwords implements ActOfGod {
	@Override
	public String getID() {
		return "WOODEN_SWORDS";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append(
				"UPDATE Units SET StrategicResource = null WHERE UnitType = 'UNIT_SWORDSMAN';\n" +
				"UPDATE Units SET StrategicResource = null WHERE UnitType in (SELECT CivUniqueUnitType FROM UnitReplaces WHERE ReplacesUnitType = 'UNIT_SWORDSMAN');\n"
		);
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
