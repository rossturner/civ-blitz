package technology.rocketjump.civimperium.matches;

import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;

public class SecretObjectiveResponse extends SecretObjective {

	private final String enumName;
	private final String objectiveName;
	private final String description;
	private final int numStars;
	private final ImperiumObjective.ObjectiveType type;

	public SecretObjectiveResponse(SecretObjective secretObjective) {
		super(secretObjective);
		ImperiumObjective objective = secretObjective.getObjective();
		this.enumName = objective.name();
		this.objectiveName = objective.objectiveName;
		this.description = objective.description;
		this.numStars = objective.numStars;
		this.type = objective.objectiveType;
	}

	public String getEnumName() {
		return enumName;
	}

	public String getObjectiveName() {
		return objectiveName;
	}

	public String getDescription() {
		return description;
	}

	public int getNumStars() {
		return numStars;
	}

	public ImperiumObjective.ObjectiveType getType() {
		return type;
	}
}
