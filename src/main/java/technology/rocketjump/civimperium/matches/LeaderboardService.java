package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.matches.objectives.AllObjectivesService;
import technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinition;
import technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinitionRepo;
import technology.rocketjump.civimperium.matches.objectives.PublicObjectiveWithClaimants;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinition.NULL_OBJECTIVE;

@Service
public class LeaderboardService {

	private final AllObjectivesService allObjectivesService;
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;

	@Autowired
	public LeaderboardService(AllObjectivesService allObjectivesService, ObjectiveDefinitionRepo objectiveDefinitionRepo) {
		this.allObjectivesService = allObjectivesService;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
	}

	public Map<String, Integer> getLeaderboard(MatchWithPlayers match) {
		Map<String, Integer> scoreboard = new LinkedHashMap<>();

		List<PublicObjectiveWithClaimants> publicObjectives = allObjectivesService.getPublicObjectives(match.getMatchId());
		List<SecretObjective> secretObjectives = allObjectivesService.getAllSecretObjectives(match, fakePlayer);

		match.signups.forEach(signup -> {
			int score = 0;

			for (PublicObjectiveWithClaimants objective : publicObjectives) {
				if (objective.getClaimedByPlayerIds().contains(signup.getPlayerId())) {
					ObjectiveDefinition objectiveDefinition = objectiveDefinitionRepo.getById(objective.getObjective()).orElse(NULL_OBJECTIVE);
					Integer stars = objectiveDefinition.getStars(match.getStartEra());
					if (stars != null) {
						score += stars;
					}
				}
			}
			for (SecretObjective secretObjective : secretObjectives) {
				if (secretObjective.getPlayerId().equals(signup.getPlayerId()) && secretObjective.getClaimed()) {
					ObjectiveDefinition objectiveDefinition = objectiveDefinitionRepo.getById(secretObjective.getObjective()).orElse(NULL_OBJECTIVE);
					Integer stars = objectiveDefinition.getStars(match.getStartEra());
					if (stars != null) {
						score += stars;
					}
				}
			}

			scoreboard.put(signup.getPlayerId(), score);
		});
		return scoreboard;
	}

	private static Player fakePlayer = new Player();
	static {
		fakePlayer.setIsAdmin(true);
	}
}
