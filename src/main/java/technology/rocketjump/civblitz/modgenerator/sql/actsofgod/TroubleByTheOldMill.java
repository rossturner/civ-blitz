package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class TroubleByTheOldMill implements ActOfGod {
	@Override
	public String getID() {
		return "TROUBLE_OLD_MILL";
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
				"INSERT OR REPLACE INTO Building_YieldChanges (BuildingType, YieldType, YieldChange) VALUES ('BUILDING_WATER_MILL', 'YIELD_PRODUCTION', 2);\n"+
				"INSERT OR REPLACE INTO Building_YieldChanges (BuildingType, YieldType, YieldChange) VALUES ('BUILDING_WATER_MILL', 'YIELD_GOLD', 2);\n"
		);
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
