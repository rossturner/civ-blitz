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

import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.PUBLIC;
import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.SECRET;
import static technology.rocketjump.civimperium.matches.MatchState.IN_PROGRESS;

@Service
public class ObjectivesService {

	private final AllObjectivesService allObjectivesService;
	private final ObjectivesRepo objectivesRepo;
	private final Random random = new Random();

	@Autowired
	public ObjectivesService(AllObjectivesService allObjectivesService, ObjectivesRepo objectivesRepo) {
		this.allObjectivesService = allObjectivesService;
		this.objectivesRepo = objectivesRepo;
	}

	public void initialiseObjectives(MatchWithPlayers match) {
		clearObjectives(match);

		List<ImperiumObjective> allPublicObjectives = Arrays.stream(ImperiumObjective.values())
				.filter(o -> o.objectiveType.equals(PUBLIC) && o.active)
				.collect(Collectors.toList());

		objectivesRepo.addPublicObjectives(match, allPublicObjectives);

		List<ImperiumObjective> allSecretObjectives = Arrays.stream(ImperiumObjective.values())
				.filter(o -> o.objectiveType.equals(SECRET) && o.active)
				.collect(Collectors.toList());

		for (MatchSignupWithPlayer signup : match.signups) {
			Set<ImperiumObjective> objectivesForPlayer = new HashSet<>();
			pickSecretObjective(1, objectivesForPlayer, allSecretObjectives);
			pickSecretObjective(2, objectivesForPlayer, allSecretObjectives);
			pickSecretObjective(3, objectivesForPlayer, allSecretObjectives);
			pickSecretObjective(null, objectivesForPlayer, allSecretObjectives);
			pickSecretObjective(null, objectivesForPlayer, allSecretObjectives);

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

	private void pickSecretObjective(Integer requiredNumStars, Set<ImperiumObjective> objectivesForPlayer, List<ImperiumObjective> allSecretObjectives) {
		ImperiumObjective selectedObjective = null;
		while (selectedObjective == null) {
			selectedObjective = allSecretObjectives.get(random.nextInt(allSecretObjectives.size()));
			if (objectivesForPlayer.contains(selectedObjective)) {
				selectedObjective = null;
			} else if (requiredNumStars != null && requiredNumStars != selectedObjective.numStars) {
				selectedObjective = null;
			}
		}
		objectivesForPlayer.add(selectedObjective);
	}

	public void claimObjective(Player player, ImperiumObjective objective, MatchWithPlayers match) {
		if (!match.signups.stream().anyMatch(s -> s.getPlayer().equals(player))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player is not part of the specified match");
		}

		if (!match.getMatchState().equals(IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only claim objectives while match is in progress");
		}

		if (objective.objectiveType.equals(SECRET)) {
			List<SecretObjective> secretObjectives = getSecretObjectives(match.getMatchId(), player);
			SecretObjective secretObjective = secretObjectives.stream().filter(s -> s.getObjective().equals(objective) && s.getSelected()).findFirst()
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have that secret objective"));

			if (secretObjective.getClaimed()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already claimed this objective");
			}

			secretObjective.setClaimed(true);
			objectivesRepo.update(secretObjective);
		} else {
			List<PublicObjectiveWithClaimants> publicObjectives = allObjectivesService.getPublicObjectives(match.getMatchId());
			PublicObjectiveWithClaimants publicObjective = publicObjectives.stream().filter(p -> p.getObjective().equals(objective)).findAny()
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "That objective is not part of this match"));

			List<String> claimedByPlayerIds = publicObjective.getClaimedByPlayerIds();
			if (claimedByPlayerIds.contains(player.getPlayerId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already claimed this objective");
			}

			if (claimedByPlayerIds.size() >= maxClaimsFor(objective)) {
				throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Other players have already claimed this objective the maximum number of times");
			}

			objectivesRepo.createClaim(objective, player, match);
		}
	}

	public void unclaimObjective(Player player, ImperiumObjective objective, MatchWithPlayers match) {
		if (!match.getMatchState().equals(IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only unclaim objectives while match is in progress");
		}

		if (objective.objectiveType.equals(SECRET)) {
			List<SecretObjective> secretObjectives = getSecretObjectives(match.getMatchId(), player);
			SecretObjective secretObjective = secretObjectives.stream().filter(s -> s.getObjective().equals(objective) && s.getSelected()).findFirst()
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have that secret objective"));
			secretObjective.setClaimed(false);
			objectivesRepo.update(secretObjective);
		} else {
			// Just try the delete, if it doesn't exist it doesn't matter
			objectivesRepo.deleteClaim(objective, player, match);
		}
	}

	private int maxClaimsFor(ImperiumObjective objective) {
		if (objective.numStars == 1) {
			return 3;
		} else if (objective.numStars == 2) {
			return 2;
		} else {
			return 1;
		}
	}
}
