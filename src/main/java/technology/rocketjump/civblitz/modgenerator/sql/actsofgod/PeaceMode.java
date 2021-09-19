package technology.rocketjump.civblitz.modgenerator.sql.actsofgod;

import java.util.List;

import static technology.rocketjump.civblitz.modgenerator.sql.actsofgod.Contractors.addTraitModifier;

public class PeaceMode implements ActOfGod {

	@Override
	public String getID() {
		return "PEACE_MODE";
	}

	@Override
	public void applyToCivTrait(String civAbilityTraitType, String modName, StringBuilder sqlBuilder) {
		for (String modifierId : List.of(
				"TRAIT_NO_SUPRISE_WAR_FOR_CANADA"
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
