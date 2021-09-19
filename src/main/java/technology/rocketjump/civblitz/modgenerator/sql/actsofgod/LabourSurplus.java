package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class LabourSurplus implements ActOfGod {
	@Override
	public String getID() {
		return "LABOUR_SURPLUS";
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
				"UPDATE Units set BuildCharges = (SELECT BuildCharges FROM Units WHERE UnitType = 'UNIT_BUILDER') + 1 WHERE UnitType = 'UNIT_BUILDER';\n"
		);
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
