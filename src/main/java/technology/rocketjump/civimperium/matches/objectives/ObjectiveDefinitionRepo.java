package technology.rocketjump.civimperium.matches.objectives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.mapgen.StartEra;

import java.util.*;

import static technology.rocketjump.civimperium.mapgen.StartEra.values;

@Component
public class ObjectiveDefinitionRepo {

	private final Map<String, ObjectiveDefinition> byObjectiveId = new HashMap<>();
	private final Map<StartEra, Map<String, ObjectiveDefinition>> activeByEra = new HashMap<>();


	@Autowired
	public ObjectiveDefinitionRepo() {
		for (StartEra value : StartEra.values()) {
			activeByEra.put(value, new HashMap<>());
		}
	}

	public void add(ObjectiveDefinition objective) {
		byObjectiveId.put(objective.objectiveId, objective);

		if (objective.active) {
			for (StartEra startEra : values()) {
				if (objective.getStars(startEra) != null && objective.active) {
					activeByEra.get(startEra).put(objective.objectiveId, objective);
				}
			}
		}
	}

	public List<ObjectiveDefinition> getActiveByEra(StartEra startEra) {
		return new ArrayList<>(activeByEra.get(startEra).values());
	}

	public Optional<ObjectiveDefinition> getById(String objectiveId) {
		return Optional.ofNullable(byObjectiveId.get(objectiveId));
	}
}
