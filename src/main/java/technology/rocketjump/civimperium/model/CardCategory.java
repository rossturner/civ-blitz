package technology.rocketjump.civimperium.model;

public enum CardCategory {

	LeaderAbility,
	CivilizationAbility,
	UniqueUnit,
	UniqueInfrastructure;

	public static CardCategory parseAcronym(String acronym) {
		switch (acronym) {
			case "UI":
				return UniqueUnit;
			case "UU":
				return UniqueInfrastructure;
			case "CA":
				return CivilizationAbility;
			default:
				throw new RuntimeException("Unrecognised CardCategory acronym " + acronym);
		}
	}
}
