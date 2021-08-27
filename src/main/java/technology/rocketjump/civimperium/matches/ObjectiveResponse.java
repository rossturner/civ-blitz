package technology.rocketjump.civimperium.matches;

import java.util.List;

public class ObjectiveResponse {

	private final String enumName;
	private final String objectiveName;
	private final String description;
	private final int numStars;
	private final ImperiumObjective.ObjectiveType type;
	private final List<String> claimedByPlayerIds;

	public ObjectiveResponse(ImperiumObjective objective, List<String> claimedByPlayerIds) {
		this.enumName = objective.name();
		this.objectiveName = objective.objectiveName;
		this.description = objective.description;
		this.numStars = objective.numStars;
		this.type = objective.objectiveType;
		this.claimedByPlayerIds = claimedByPlayerIds;
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

	public List<String> getClaimedByPlayerIds() {
		return claimedByPlayerIds;
	}
}
