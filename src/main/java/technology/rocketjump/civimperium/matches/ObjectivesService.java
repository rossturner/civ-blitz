package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

	public List<SecretObjective> getAllSecretObjectives(MatchWithPlayers match, Player player) {
		if (match.getMatchState().equals(MatchState.IN_PROGRESS)) {
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be an admin to get all secrets while match is in progress");
			} else if (match.signups.stream().anyMatch(s -> s.getPlayer().equals(player))) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not get all secrets when current player is in the match");
			} else {
				// Is an admin not in the match, can get all secrets
				return objectivesRepo.getAllSecretObjectives(match.getMatchId());
			}
		} else if (match.getMatchState().equals(MatchState.POST_MATCH)) {
			// At this state anyone can get all secret objectives
			return objectivesRepo.getAllSecretObjectives(match.getMatchId());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not retrieve all secret objectives in state " + match.getMatchState());
		}
	}

	public void update(SecretObjective secretObjective) {
		objectivesRepo.update(secretObjective);
	}
}
