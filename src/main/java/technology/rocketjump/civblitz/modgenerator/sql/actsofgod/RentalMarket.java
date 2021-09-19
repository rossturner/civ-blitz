package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class RentalMarket implements ActOfGod {
	@Override
	public String getID() {
		return "RENTAL_MARKET";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE StartEras SET StartingHousingCapital = 2 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingHousingCapital = 4 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingHousingCapital = 7 WHERE EraType = 'ERA_INDUSTRIAL';\n");

		sqlBuilder.append("UPDATE StartEras SET StartingHousingOtherCities = 2 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingHousingOtherCities = 3 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET StartingHousingOtherCities = 5 WHERE EraType = 'ERA_INDUSTRIAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
