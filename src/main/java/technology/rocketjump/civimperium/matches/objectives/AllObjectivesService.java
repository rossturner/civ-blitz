package technology.rocketjump.civimperium.matches.objectives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.matches.MatchState;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.List;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.matches.MatchState.IN_PROGRESS;

@Service
public class AllObjectivesService {

	private final ObjectivesRepo objectivesRepo;

	@Autowired
	public AllObjectivesService(ObjectivesRepo objectivesRepo) {
		this.objectivesRepo = objectivesRepo;
	}

	public List<PublicObjectiveWithClaimants> getPublicObjectives(int matchId) {
		return objectivesRepo.getAllPublicObjectives(matchId);
	}

	public List<SecretObjective> getAllSecretObjectives(MatchWithPlayers match, Player player) {
		if (match.getMatchState().equals(IN_PROGRESS)) {
			boolean playerIsAdminAndNotInMatch = player.getIsAdmin() &&
					!match.signups.stream().anyMatch(s -> s.getPlayer().equals(player));
			return objectivesRepo.getAllSecretObjectives(match.getMatchId())
					.stream().filter(s -> s.getClaimed() || s.getPlayerId().equals(player.getPlayerId()) || playerIsAdminAndNotInMatch)
					.collect(Collectors.toList());
		} else if (match.getMatchState().equals(MatchState.POST_MATCH)) {
			// At this state anyone can get all secret objectives
			return objectivesRepo.getAllSecretObjectives(match.getMatchId());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not retrieve all secret objectives in state " + match.getMatchState());
		}
	}

}
