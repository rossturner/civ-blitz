package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class LeapOfFaith implements ActOfGod {
	@Override
	public String getID() {
		return "LEAP_OF_FAITH";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE StartEras SET Faith = 0 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET Faith = 40 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET Faith = 800 WHERE EraType = 'ERA_INDUSTRIAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
