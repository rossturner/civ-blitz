package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class ThreeForFree implements ActOfGod {
	@Override
	public String getID() {
		return "THREE_FOR_FREE";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE StartEras SET Tiles = 3 WHERE EraType = 'ERA_ANCIENT';\n");
		sqlBuilder.append("UPDATE StartEras SET Tiles = 5 WHERE EraType = 'ERA_MEDIEVAL';\n");
		sqlBuilder.append("UPDATE StartEras SET Tiles = 7 WHERE EraType = 'ERA_INDUSTRIAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
