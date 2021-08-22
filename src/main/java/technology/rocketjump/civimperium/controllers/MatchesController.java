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
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.matches.*;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;
import technology.rocketjump.civimperium.model.MatchWithPlayers;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.PUBLIC;
import static technology.rocketjump.civimperium.matches.MatchState.DRAFT;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final MatchService matchService;
	private final ObjectivesService objectivesService;
	private final AuditLogger auditLogger;

	@Autowired
	public MatchesController(JwtService jwtService, PlayerService playerService, MatchService matchService,
							 ObjectivesService objectivesService, AuditLogger auditLogger) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.matchService = matchService;
		this.objectivesService = objectivesService;
		this.auditLogger = auditLogger;
	}

	@GetMapping
	public List<MatchWithPlayers> getUncompletedMatches(@RequestHeader("Authorization") String jwToken) {
		List<MatchWithPlayers> matches = matchService.getUncompletedMatches();
		String currentPlayerId = jwToken == null ? null : jwtService.parse(jwToken).getDiscordId();
		matches.forEach(match -> {
			if (match.getMatchState().equals(DRAFT)) {
				// Hide other players' selections in draft stage
				match.signups.forEach(signup -> {
					if (!signup.getPlayerId().equals(currentPlayerId)) {
						signup.hideAllCards();
					}
				});
			}
		});
		return matches;
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

	@GetMapping("/{matchId}")
	public Match getMatch(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (match.getMatchState().equals(DRAFT)) {
			// Hide other players' selections in draft stage
			String currentPlayerId = jwToken == null ? null : jwtService.parse(jwToken).getDiscordId();
			match.signups.forEach(signup -> {
				if (!signup.getPlayerId().equals(currentPlayerId)) {
					signup.hideAllCards();
				}
			});
		}
		return match;
	}

	@GetMapping("/{matchId}/public_objectives")
	public List<ObjectiveResponse> getMatchObjectives(@PathVariable int matchId) {
		// TODO replace this with cards actually selected for the match
		return Arrays.stream(ImperiumObjective.values()).filter(o -> o.objectiveType.equals(PUBLIC))
				.map(ObjectiveResponse::new)
				.sorted((o1, o2) -> o1.getNumStars() == o2.getNumStars() ? o1.getObjectiveName().compareTo(o2.getObjectiveName()) : o2.getNumStars() - o1.getNumStars())
				.collect(Collectors.toList());
	}

	@GetMapping("/{matchId}/secret_objectives")
	public List<SecretObjectiveResponse> getMatchSecretObjectives(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			return objectivesService.getSecretObjectives(matchId, player).stream()
					.map(SecretObjectiveResponse::new)
					.collect(Collectors.toList());
		}
	}

	@GetMapping("/{matchId}/all_secret_objectives")
	public List<SecretObjectiveResponse> getAllMatchSecretObjectives(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			return objectivesService.getAllSecretObjectives(match, player).stream()
					.map(SecretObjectiveResponse::new)
					.collect(Collectors.toList());
		}
	}

	@PostMapping("/{matchId}/secret_objectives/{objective}")
	public List<SecretObjectiveResponse> updateMatchSecretObjectives(@RequestHeader("Authorization") String jwToken,
																	 @PathVariable int matchId,
																	 @PathVariable String objective) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			List<SecretObjective> secretObjectives = objectivesService.getSecretObjectives(matchId, player);
			Optional<SecretObjective> chosenObjective = secretObjectives
					.stream().filter(o -> o.getObjective().name().equals(objective))
					.findFirst();
			if (chosenObjective.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				SecretObjective secretObjective = chosenObjective.get();

				if (!secretObjective.getSelected() && secretObjectives.stream().filter(SecretObjective::getSelected).count() == 2) {
					// already have two other objectives selected
				} else {
					secretObjective.setSelected(!secretObjective.getSelected());
					matchService.updateSecretObjectiveSelection(secretObjective);
				}

				return secretObjectives.stream().map(SecretObjectiveResponse::new).collect(Collectors.toList());
			}
		}
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
				Optional<MatchWithPlayers> originalMatch = matchService.getById(matchId);
				if (originalMatch.isEmpty()) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				} else {
					Match original = originalMatch.get();

					String timeslot = payload.get("matchTimeslot").toString();
					String name = payload.get("matchName").toString();
					Match match = matchService.updateSecretObjectiveSelection(new Match(original), name, timeslot);

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

	@PostMapping("/{matchId}/cards")
	public MatchSignupWithPlayer addCardToMatchDeck(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId,
													@RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			Optional<MatchWithPlayers> match = matchService.getById(matchId);
			if (match.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				String cardTraitType = payload.get("cardTraitType").toString();
				boolean applyFreeUse = payload.containsKey("applyFreeUse") && (boolean) payload.get("applyFreeUse");
				return matchService.addCardToMatchDeck(match.get(), player, cardTraitType, applyFreeUse);
			}
		}
	}

	@PostMapping("/{matchId}/cards/remove")
	public MatchSignupWithPlayer removeCardFromMatchDeck(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId,
														 @RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			Optional<MatchWithPlayers> match = matchService.getById(matchId);
			if (match.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				String cardTraitType = payload.get("cardTraitType").toString();
				return matchService.removeCardFromMatchDeck(match.get(), player, cardTraitType);
			}
		}
	}

	@PostMapping("/{matchId}/bias")
	public MatchSignupWithPlayer setBiasForSignup(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId,
												  @RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			Optional<MatchWithPlayers> match = matchService.getById(matchId);
			if (match.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				String biasCivType = payload.get("startBiasCivType").toString();
				return matchService.updateStartBias(match.get(), player, biasCivType);
			}
		}
	}

	@PostMapping("/{matchId}/commit")
	public MatchSignupWithPlayer commitSignup(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			Optional<MatchWithPlayers> match = matchService.getById(matchId);
			if (match.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				return matchService.commitPlayer(match.get(), player);
			}
		}
	}

	@DeleteMapping("/{matchId}/commit")
	public MatchSignupWithPlayer uncommitSignup(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			Optional<MatchWithPlayers> match = matchService.getById(matchId);
			if (match.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				return matchService.uncommitPlayer(match.get(), player);
			}
		}
	}

	@PostMapping("/{matchId}/players")
	public void signupToMatch(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			matchService.signup(matchId, player);
		}
	}

	@PutMapping("/{matchId}/{matchState}")
	public Match switchMatchState(@RequestHeader("Authorization") String jwToken,
								  @RequestBody Map<String, Object> payload, @PathVariable int matchId, @PathVariable MatchState matchState) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				Optional<MatchWithPlayers> match = matchService.getById(matchId);
				if (match.isEmpty()) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				} else {
					Match result = matchService.switchState(match.get(), matchState, payload);
					auditLogger.record(player, "Changed state of match " + result.getMatchName() + " to " + result.getMatchState());
					return result;
				}
			}
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
