package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class NavySteals implements ActOfGod {
	@Override
	public String getID() {
		return "NAVY_STEALS";
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
				"INSERT into GreatPersonIndividualActionModifiers (GreatPersonIndividualType, ModifierId, AttachmentTargetType) \n" +
				"SELECT GreatPersonIndividualType, 'GREATPERSON_BOUDICA_ACTIVE', 'GREAT_PERSON_ACTION_ATTACHMENT_TARGET_UNIT_GREATPERSON' \n" +
				"from GreatPersonIndividuals where GreatPersonClassType = 'GREAT_PERSON_CLASS_ADMIRAL';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
