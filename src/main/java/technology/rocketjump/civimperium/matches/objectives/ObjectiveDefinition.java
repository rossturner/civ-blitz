package technology.rocketjump.civimperium.matches.objectives;

import technology.rocketjump.civimperium.mapgen.StartEra;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ObjectiveDefinition {

	public static final ObjectiveDefinition NULL_OBJECTIVE = new ObjectiveDefinition("Unknown", "UNKNOWN",
			"This objective is missing", ObjectiveType.PUBLIC, false, false);
	static {
		for (StartEra startEra : StartEra.values()) {
			NULL_OBJECTIVE.setStars(startEra, 0);
		}
	}

	public final String objectiveId;
	public final String objectiveName;
	public final String description;

	private Map<StartEra, Integer> starsByEra = new EnumMap<>(StartEra.class);

	public final ObjectiveType objectiveType;
	public final boolean military;
	public final boolean active;

	public enum ObjectiveType {
		PUBLIC, SECRET;
	}
	public ObjectiveDefinition(String objectiveName, String objectiveId, String description, ObjectiveType objectiveType, boolean military, boolean active) {
		this.objectiveName = objectiveName;
		this.objectiveId = objectiveId;
		this.description = description;
		this.objectiveType = objectiveType;
		this.military = military;
		this.active = active;
	}

	public void setStars(StartEra startEra, Integer numStars) {
		starsByEra.put(startEra, numStars);
	}

	public Integer getStars(StartEra startEra) {
		return starsByEra.get(startEra);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObjectiveDefinition that = (ObjectiveDefinition) o;
		return objectiveId.equals(that.objectiveId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectiveId);
	}
}
