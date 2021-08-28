package technology.rocketjump.civimperium.matches.objectives;

import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.mapgen.StartEra;

public class SecretObjectiveResponse extends SecretObjective {

	private final String enumName;
	private final String objectiveName;
	private final String description;
	private final int numStars;
	private final ObjectiveDefinition.ObjectiveType type;

	public SecretObjectiveResponse(SecretObjective secretObjective, ObjectiveDefinition objectiveDefinition, StartEra startEra) {
		super(secretObjective);
		this.enumName = objectiveDefinition.objectiveId;
		this.objectiveName = objectiveDefinition.objectiveName;
		this.description = objectiveDefinition.description;
		Integer stars = objectiveDefinition.getStars(startEra);
		this.numStars = stars != null ? stars : 0;
		this.type = objectiveDefinition.objectiveType;
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

	public ObjectiveDefinition.ObjectiveType getType() {
		return type;
	}
}
