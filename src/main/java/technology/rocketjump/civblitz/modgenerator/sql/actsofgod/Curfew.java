package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class Curfew implements ActOfGod {
	@Override
	public String getID() {
		return "CURFEW";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 6 WHERE ModifierId = 'LIMITANEI_GARRISONIDENTITY' and Name = 'Amount';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_LIMITANEI_NAME", "Curfew", sqlBuilder);
		addTranslation("LOC_POLICY_LIMITANEI_DESCRIPTION", "+6 Loyalty per turn for cities with a garrisoned unit.", sqlBuilder);
	}

}
