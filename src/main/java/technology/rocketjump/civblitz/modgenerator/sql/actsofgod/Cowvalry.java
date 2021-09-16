package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.Map;

import static technology.rocketjump.civblitz.modgenerator.sql.StringTemplateWrapper.ST;

public class Cowvalry implements ActOfGod {
	@Override
	public String getID() {
		return "COWVALRY";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {

	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {

		sqlBuilder.append("UPDATE Resources SET Frequency = 12, ResourceClassType = 'RESOURCECLASS_STRATEGIC', " +
				"PrereqTech = 'TECH_ANIMAL_HUSBANDRY' WHERE ResourceType = 'RESOURCE_CATTLE';\n");
		sqlBuilder.append("UPDATE Resources SET Frequency = 6, ResourceClassType = 'RESOURCECLASS_BONUS', " +
				"PrereqTech = null WHERE ResourceType = 'RESOURCE_HORSES';\n");

		sqlBuilder.append(
				"INSERT OR REPLACE INTO Resource_YieldChanges (ResourceType, YieldType, YieldChange) VALUES ('RESOURCE_CATTLE', 'YIELD_PRODUCTION', 1);\n" +
				"INSERT OR REPLACE INTO Resource_YieldChanges (ResourceType, YieldType, YieldChange) VALUES ('RESOURCE_CATTLE', 'YIELD_GOLD', 1);\n"
		);

		sqlBuilder.append("UPDATE Resource_Consumption SET ResourceType = 'RESOURCE_CATTLE' WHERE ResourceType = 'RESOURCE_HORSES';\n");
		sqlBuilder.append("UPDATE Resource_Harvests SET ResourceType = 'RESOURCE_HORSES' WHERE ResourceType = 'RESOURCE_CATTLE';\n");

		sqlBuilder.append("UPDATE Units SET BaseMoves = 3, Combat = 39 WHERE UnitType = 'UNIT_HORSEMAN';\n");
		sqlBuilder.append("UPDATE Units SET StrategicResource = 'RESOURCE_CATTLE' WHERE StrategicResource = 'RESOURCE_HORSES';\n");
	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {
		addTranslation("LOC_TECH_HORSEBACK_RIDING_NAME", "Cowback Riding", sqlBuilder);
		addTranslation("LOC_UNIT_HORSEMAN_NAME", "Cowman", sqlBuilder);
		addTranslation("LOC_UNIT_HORSEMAN_DESCRIPTION", "A cow-mounted warrior with high strength and speed.", sqlBuilder);
		addTranslation("LOC_UNIT_CAVALRY_NAME", "Cowvalry", sqlBuilder);
		addTranslation("LOC_UNIT_BARBARIAN_HORSEMAN_NAME", "Barbarian Cowman", sqlBuilder);
	}

	public static void addTranslation(String tag, String text, StringBuilder sqlBuilder) {
		sqlBuilder.append(ST("INSERT OR REPLACE INTO LocalizedText (Tag, Language, Text) VALUES " +
				"('<tag>', 'en_US', '<text>');\n", Map.of(
				"tag", tag,
				"text", text
		)));
	}
}
