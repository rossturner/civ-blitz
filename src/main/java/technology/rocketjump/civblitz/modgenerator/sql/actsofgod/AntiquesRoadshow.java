package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.List;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Contractors.addTraitModifier;

public class AntiquesRoadshow implements ActOfGod {

	@Override
	public String getID() {
		return "ANTIQUES_ROADSHOW";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		for (String modifierId : List.of(
				"TRAIT_LITHUANIANUNION_FAITH_RELIC",
				"TRAIT_LITHUANIANUNION_CULTURE_RELIC",
				"TRAIT_LITHUANIANUNION_GOLD_RELIC"
		)) {
			addTraitModifier(modifierId, civAbilityTraitType, sqlBuilder);
		}
	}

	@Override
	public void applyToLeaderTrait(String leaderAbilityTraitType, String modName, StringBuilder sqlBuilder) {
	}

	@Override
	public void applyGlobalChanges(StringBuilder sqlBuilder) {

	}

	@Override
	public void applyLocalisationChanges(StringBuilder sqlBuilder) {

	}
}
