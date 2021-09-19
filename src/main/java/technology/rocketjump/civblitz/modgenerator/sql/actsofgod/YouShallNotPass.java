package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class YouShallNotPass implements ActOfGod {
	@Override
	public String getID() {
		return "SHALL_NOT_PASS";
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
				"UPDATE Buildings SET OuterDefenseHitPoints = 150 WHERE BuildingType = 'BUILDING_WALLS';\n"
		);
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
