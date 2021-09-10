package technology.rocketjump.civblitz.matches.objectives;

import technology.rocketjump.civblitz.mapgen.StartEra;

import java.util.List;

public class ObjectiveResponse {

	private final String enumName;
	private final String objectiveName;
	private final String description;
	private final int numStars;
	private final ObjectiveDefinition.ObjectiveType type;
	private final List<String> claimedByPlayerIds;
	private final boolean military;

	public ObjectiveResponse(ObjectiveDefinition objective, List<String> claimedByPlayerIds, StartEra startEra) {
		this.enumName = objective.objectiveId;
		this.objectiveName = objective.objectiveName;
		this.description = objective.description;
		Integer stars = objective.getStars(startEra);
		this.numStars = stars != null ? stars : 0;
		this.type = objective.objectiveType;
		this.claimedByPlayerIds = claimedByPlayerIds;
		this.military = objective.military;
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

	public List<String> getClaimedByPlayerIds() {
		return claimedByPlayerIds;
	}

	public boolean isMilitary() {
		return military;
	}
}
