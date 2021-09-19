package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class Veganism implements ActOfGod {
	@Override
	public String getID() {
		return "VEGANISM";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE Resource_YieldChanges SET YieldType = 'YIELD_FAITH' WHERE ResourceType in ('RESOURCE_CATTLE', 'RESOURCE_FISH', 'RESOURCE_SHEEP') and YieldType = 'YIELD_FOOD';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}

}
