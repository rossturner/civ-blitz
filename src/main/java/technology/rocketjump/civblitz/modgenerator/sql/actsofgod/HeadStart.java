package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class HeadStart implements ActOfGod {
	@Override
	public String getID() {
		return "HEAD_START";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE StartEras SET StartingPopulationCapital = 2 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingPopulationCapital = 6 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingPopulationCapital = 11 WHERE EraType = 'ERA_INDUSTRIAL';\n");

		sqlBuilder.append("UPDATE StartEras SET StartingPopulationOtherCities = 2 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingPopulationOtherCities = 3 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingPopulationOtherCities = 6 WHERE EraType = 'ERA_INDUSTRIAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
