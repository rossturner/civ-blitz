package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.*;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.SECRET;

@Service
public class ObjectivesService {

	private static final int SECRET_OBJECTIVES_PER_PLAYER = 3;

	private final ObjectivesRepo objectivesRepo;
	private final Random random = new Random();

	@Autowired
	public ObjectivesService(ObjectivesRepo objectivesRepo) {
		this.objectivesRepo = objectivesRepo;
	}

	public void initialiseObjectives(MatchWithPlayers match) {
		clearObjectives(match);
		List<ImperiumObjective> allSecretObjectives = Arrays.stream(ImperiumObjective.values())
				.filter(o -> o.objectiveType.equals(SECRET))
				.collect(Collectors.toList());

		for (MatchSignupWithPlayer signup : match.signups) {
			Set<ImperiumObjective> objectivesForPlayer = new HashSet<>();
			while (objectivesForPlayer.size() < SECRET_OBJECTIVES_PER_PLAYER) {
				objectivesForPlayer.add(allSecretObjectives.get(random.nextInt(allSecretObjectives.size())));
			}

			for (ImperiumObjective objective : objectivesForPlayer) {
				objectivesRepo.add(match, signup, objective);
			}
		}
	}

	public void clearObjectives(Match match) {
		objectivesRepo.clear(match);
	}

	public List<SecretObjective> getSecretObjectives(int matchId, Player player) {
		return objectivesRepo.getSecretObjectives(matchId, player);
	}

	public void update(SecretObjective secretObjective) {
		objectivesRepo.update(secretObjective);
	}
}
