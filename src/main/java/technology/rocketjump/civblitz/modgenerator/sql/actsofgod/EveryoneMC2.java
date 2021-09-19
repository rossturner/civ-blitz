package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public class EveryoneMC2 implements ActOfGod {
	@Override
	public String getID() {
		return "EVERYONE_MC2";
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
				"SELECT GreatPersonIndividualType, 'GREATPERSON_RESEARCHLABS_BIG_SCIENCE', 'GREAT_PERSON_ACTION_ATTACHMENT_TARGET_DISTRICT_IN_TILE' \n" +
				"from GreatPersonIndividuals where GreatPersonClassType = 'GREAT_PERSON_CLASS_SCIENTIST' and EraType in ('ERA_INDUSTRIAL', 'ERA_MODERN') and GreatPersonIndividualType != 'GREAT_PERSON_INDIVIDUAL_ALBERT_EINSTEIN';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
	}
}
