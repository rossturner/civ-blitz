package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.auth.AuditLogger;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.matches.MatchService;
import technology.rocketjump.civimperium.model.MatchWithPlayers;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final MatchService matchService;
	private final AuditLogger auditLogger;

	@Autowired
	public MatchesController(JwtService jwtService, PlayerService playerService, MatchService matchService, AuditLogger auditLogger) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.matchService = matchService;
		this.auditLogger = auditLogger;
	}

	@PostMapping
	public Match createMatch(@RequestHeader("Authorization") String jwToken,
							 @RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else if (!payload.containsKey("matchTimeslot")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			} else {
				String timeslot = payload.get("matchTimeslot").toString();
				String name = payload.containsKey("matchName") ? payload.get("matchName").toString() : null;
				Match match = matchService.create(name, timeslot);
				auditLogger.record(player, "Created a new match: " + match.getMatchName());
				return match;
			}
		}
	}

	@GetMapping
	public List<MatchWithPlayers> getUncompletedMatches() {
		return matchService.getUncompletedMatches();
	}

	@PostMapping("/{matchId}")
	public Match editMatch(@RequestHeader("Authorization") String jwToken,
							 @RequestBody Map<String, Object> payload, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				Optional<Match> originalMatch = matchService.getById(matchId);
				if (originalMatch.isEmpty()) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				} else {
					Match original = originalMatch.get();

					String timeslot = payload.get("matchTimeslot").toString();
					String name = payload.get("matchName").toString();
					Match match = matchService.update(new Match(original), name, timeslot);

					StringBuilder auditDescription = new StringBuilder();
					auditDescription.append("Edited match ").append(original.getMatchName());
					if (!original.getMatchName().equals(match.getMatchName())) {
						auditDescription.append(", changed name from '").append(original.getMatchName()).append("' to '").append(match.getMatchName()).append("'");
					}
					if (!original.getTimeslot().equals(match.getTimeslot())) {
						auditDescription.append(", changed timeslot from '").append(original.getTimeslot()).append("' to '").append(match.getTimeslot()).append("'");
					}
					auditLogger.record(player, auditDescription.toString());
					return match;
				}
			}
		}
	}

	@PutMapping("/{matchId}/players")
	public void signupToMatch(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			matchService.signup(matchId, player);
		}
	}

	@DeleteMapping("/{matchId}/players")
	public void resignFromMatch(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			matchService.resign(matchId, player);
		}
	}

}
