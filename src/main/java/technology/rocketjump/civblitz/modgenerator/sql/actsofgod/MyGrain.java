package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class MyGrain implements ActOfGod {
	@Override
	public String getID() {
		return "MY_GRAIN";
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
				"INSERT OR REPLACE INTO Building_YieldChanges (BuildingType, YieldType, YieldChange) VALUES ('BUILDING_GRANARY', 'YIELD_FOOD', 3);\n"
		);
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
