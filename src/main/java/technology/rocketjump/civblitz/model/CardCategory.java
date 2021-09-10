package technology.rocketjump.civblitz.model;

public enum CardCategory {

	LeaderAbility,
	CivilizationAbility,
	UniqueUnit,
	UniqueInfrastructure;

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
