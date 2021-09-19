package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class UrbanStrategy implements ActOfGod {
	@Override
	public String getID() {
		return "URBAN_STRATEGY";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 2 WHERE ModifierId = 'URBAN_PLANNING_ALLCITYPRODUCTION' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_URBAN_PLANNING_NAME", "Urban Strategy", sqlBuilder);
		addTranslation("LOC_POLICY_URBAN_PLANNING_DESCRIPTION", "+2 [ICON_Production] Production in all cities.", sqlBuilder);
	}

}
