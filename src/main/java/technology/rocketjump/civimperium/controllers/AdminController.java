package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.auth.AuditLogger;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.codegen.tables.pojos.AuditLog;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.matches.MatchService;
import technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinition;
import technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinitionRepo;
import technology.rocketjump.civimperium.matches.objectives.ObjectivesService;
import technology.rocketjump.civimperium.model.MatchWithPlayers;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final AuditLogger auditLogger;
	private final MatchService matchService;
	private final ObjectivesService objectivesService;
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;

	@Autowired
	public AdminController(JwtService jwtService, PlayerService playerService, AuditLogger auditLogger,
						   MatchService matchService, ObjectivesService objectivesService, ObjectiveDefinitionRepo objectiveDefinitionRepo) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.auditLogger = auditLogger;
		this.matchService = matchService;
		this.objectivesService = objectivesService;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
	}

	@GetMapping("/audit_logs")
	public List<AuditLog> getAuditLogs(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				return auditLogger.getRecentLogs();
			}
		}
	}


	@PostMapping("{playerId}/matches/{matchId}/objectives/{objectiveId}")
	public void forceClaimObjective(@RequestHeader("Authorization") String jwToken,
									@PathVariable String playerId,
									@PathVariable int matchId,
							   @PathVariable String objectiveId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player adminPlayer = playerService.getPlayer(token);
			if (!adminPlayer.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

				if (match.signups.stream().anyMatch(s -> s.getPlayer().equals(adminPlayer))) {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not admin results in a game you are participating in");
				}

				Player targetPlayer = playerService.getPlayerById(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
				ObjectiveDefinition objective = objectiveDefinitionRepo.getById(objectiveId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find objective with ID " + objectiveId));

				objectivesService.claimObjective(targetPlayer, objective, match);
				auditLogger.record(adminPlayer, "Claimed " + objective.objectiveName + " for " + targetPlayer.getDiscordUsername() + " in " + match.getMatchName(), match);
			}
		}
	}

	@DeleteMapping("{playerId}/matches/{matchId}/objectives/{objectiveId}")
	public void forceUnclaimObjective(@RequestHeader("Authorization") String jwToken,
									  @PathVariable String playerId,
									  @PathVariable int matchId,
								 @PathVariable String objectiveId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player adminPlayer = playerService.getPlayer(token);
			if (!adminPlayer.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

				if (match.signups.stream().anyMatch(s -> s.getPlayer().equals(adminPlayer))) {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not admin results in a game you are participating in");
				}

				Player targetPlayer = playerService.getPlayerById(playerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
				ObjectiveDefinition objective = objectiveDefinitionRepo.getById(objectiveId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find objective with ID " + objectiveId));

				objectivesService.unclaimObjective(targetPlayer, objective, match);
				auditLogger.record(adminPlayer, "Removed claim on " + objective.objectiveName + " for " + targetPlayer.getDiscordUsername() + " in " + match.getMatchName(), match);
			}
		}
	}

}
