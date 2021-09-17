package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class MonumentsToTheGods implements ActOfGod {
	@Override
	public String getID() {
		return "MONUMENT_GODS";
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
				"INSERT OR REPLACE INTO Building_YieldChanges (BuildingType, YieldType, YieldChange) VALUES ('BUILDING_MONUMENT', 'YIELD_FAITH', 2);\n"
		);

	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
