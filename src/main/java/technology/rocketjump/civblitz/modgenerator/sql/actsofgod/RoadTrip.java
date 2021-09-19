package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class RoadTrip implements ActOfGod {
	@Override
	public String getID() {
		return "ROAD_TRIP";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE Routes SET MovementCost = 0.75 WHERE RouteType = 'ROUTE_ANCIENT_ROAD';\n");
		sqlBuilder.append("UPDATE Routes SET MovementCost = 0.75 WHERE RouteType = 'ROUTE_MEDIEVAL_ROAD';\n");
		sqlBuilder.append("UPDATE Routes SET MovementCost = 0.5 WHERE RouteType = 'ROUTE_INDUSTRIAL_ROAD';\n");
		sqlBuilder.append("UPDATE Routes SET MovementCost = 0.25 WHERE RouteType = 'ROUTE_MODERN_ROAD';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
