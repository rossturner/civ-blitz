package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaderboardService {

	private final AllObjectivesService allObjectivesService;

	@Autowired
	public LeaderboardService(AllObjectivesService allObjectivesService) {
		this.allObjectivesService = allObjectivesService;
	}

	public Map<String, Integer> getLeaderboard(MatchWithPlayers match) {
		Map<String, Integer> scoreboard = new LinkedHashMap<>();

		List<PublicObjectiveWithClaimants> publicObjectives = allObjectivesService.getPublicObjectives(match.getMatchId());
		List<SecretObjective> secretObjectives = allObjectivesService.getAllSecretObjectives(match, fakePlayer);

		match.signups.forEach(signup -> {
			int score = 0;

			for (PublicObjectiveWithClaimants objective : publicObjectives) {
				if (objective.getClaimedByPlayerIds().contains(signup.getPlayerId())) {
					score += objective.getObjective().numStars;
				}
			}
			for (SecretObjective secretObjective : secretObjectives) {
				if (secretObjective.getPlayerId().equals(signup.getPlayerId()) && secretObjective.getClaimed()) {
					score += secretObjective.getObjective().numStars;
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
