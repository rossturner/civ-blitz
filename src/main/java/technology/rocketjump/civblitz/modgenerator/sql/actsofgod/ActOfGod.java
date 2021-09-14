package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

public interface ActOfGod {

	String getID();

	void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder);

	void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder);

	void applyGlobalChanges(StringBuilder sqlBuilder);

}
