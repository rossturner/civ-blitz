package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class QOLImprovements implements ActOfGod {
	@Override
	public String getID() {
		return "QOL_IMPROVEMENTS";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesCapital = 1 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesCapital = 2 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesCapital = 4 WHERE EraType = 'ERA_INDUSTRIAL';\n");

		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesOtherCities = 1 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesOtherCities = 1 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingAmenitiesOtherCities = 2 WHERE EraType = 'ERA_INDUSTRIAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
