package technology.rocketjump.civblitz.model;

import java.util.List;

public enum CardCategory {

	CivilizationAbility,
	LeaderAbility,
	UniqueUnit,
	UniqueInfrastructure,

	Power,
	ActOfGod;

	public static List<CardCategory> mainCategories = List.of(CivilizationAbility, LeaderAbility, UniqueUnit, UniqueInfrastructure);

	public boolean isUpgradable() {
		return this.equals(UniqueUnit) || this.equals(UniqueInfrastructure);
	}

	public static CardCategory parseAcronym(String acronym) {
		switch (acronym) {
			case "UI":
				return UniqueInfrastructure;
			case "UU":
				return UniqueUnit;
			case "CA":
				return CivilizationAbility;
			default:
				throw new RuntimeException("Unrecognised CardCategory acronym " + acronym);
		}
	}
}
