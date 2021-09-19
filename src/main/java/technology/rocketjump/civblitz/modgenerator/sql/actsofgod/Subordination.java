package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Cowvalry.addTranslation;

public class Subordination implements ActOfGod {
	@Override
	public String getID() {
		return "SUBORDINATION";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {
		sqlBuilder.append("UPDATE ModifierArguments SET Value = 10 WHERE ModifierId = 'DISCIPLINE_BARBARIANCOMBAT';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_POLICY_DISCIPLINE_NAME", "Subordination", sqlBuilder);
		addTranslation("LOC_POLICY_DISCIPLINE_DESCRIPTION", "+10 [ICON_Strength] Unit Combat Strength when fighting Barbarians.", sqlBuilder);
	}

}
