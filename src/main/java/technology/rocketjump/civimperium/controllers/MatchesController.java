package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.auth.AuditLogger;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civimperium.matches.LeaderboardService;
import technology.rocketjump.civimperium.matches.MatchService;
import technology.rocketjump.civimperium.matches.MatchState;
import technology.rocketjump.civimperium.matches.objectives.*;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;
import technology.rocketjump.civimperium.model.MatchWithPlayers;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.matches.MatchState.DRAFT;
import static technology.rocketjump.civimperium.matches.objectives.ObjectiveDefinition.NULL_OBJECTIVE;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {

	private static final Comparator<ObjectiveResponse> OBJECTIVE_SORT = (o1, o2) -> o1.getNumStars() == o2.getNumStars() ? o1.getObjectiveName().compareTo(o2.getObjectiveName()) : o1.getNumStars() - o2.getNumStars();
	private static final Comparator<SecretObjectiveResponse> SECRET_OBJECTIVE_SORT = (o1, o2) -> o1.getNumStars() == o2.getNumStars() ? o1.getObjectiveName().compareTo(o2.getObjectiveName()) : o1.getNumStars() - o2.getNumStars();

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final MatchService matchService;
	private final ObjectivesService objectivesService;
	private final LeaderboardService leaderboardService;
	private final AuditLogger auditLogger;
	private final AllObjectivesService allObjectivesService;
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;

	@Autowired
	public MatchesController(JwtService jwtService, PlayerService playerService, MatchService matchService,
							 ObjectivesService objectivesService, LeaderboardService leaderboardService,
							 AuditLogger auditLogger, AllObjectivesService allObjectivesService, ObjectiveDefinitionRepo objectiveDefinitionRepo) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.matchService = matchService;
		this.objectivesService = objectivesService;
		this.leaderboardService = leaderboardService;
		this.auditLogger = auditLogger;
		this.allObjectivesService = allObjectivesService;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
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
				auditLogger.record(player, "Created a new match: " + match.getMatchName(), match);
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

	@DeleteMapping("/{matchId}")
	public void deleteMatch(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
				matchService.delete(match);
				auditLogger.record(player, "Deleted match: " + match.getMatchName(), match);
			}
		}
	}

	@GetMapping("/{matchId}/leaderboard")
	public Map<String, Integer> getLeaderboard(@PathVariable int matchId) {
		MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return leaderboardService.getLeaderboard(match);
	}

	@GetMapping("/{matchId}/public_objectives")
	public List<ObjectiveResponse> getMatchObjectives(@PathVariable int matchId) {
		MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return allObjectivesService.getPublicObjectives(matchId).stream()
				.map((pub) -> {
					ObjectiveDefinition objective = objectiveDefinitionRepo.getById(pub.getObjective()).orElse(NULL_OBJECTIVE);
					return new ObjectiveResponse(objective, pub.getClaimedByPlayerIds(), match.getStartEra());
				})
				.sorted(OBJECTIVE_SORT)
				.collect(Collectors.toList());
	}

	@GetMapping("/{matchId}/secret_objectives")
	public List<SecretObjectiveResponse> getMatchSecretObjectives(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			return objectivesService.getSecretObjectives(matchId, player).stream()
					.map(secretObjective -> new SecretObjectiveResponse(secretObjective, objectiveDefinitionRepo.getById(secretObjective.getObjective()).orElse(NULL_OBJECTIVE), match.getStartEra()
					))
					.sorted(SECRET_OBJECTIVE_SORT)
					.collect(Collectors.toList());
		}
	}

	@PostMapping("/{matchId}/objectives/{objectiveId}")
	public void claimObjective(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId,
														@PathVariable String objectiveId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			ObjectiveDefinition objective = objectiveDefinitionRepo.getById(objectiveId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			objectivesService.claimObjective(player, objective, match);
			matchService.checkForWinner(match);
		}
	}

	@DeleteMapping("/{matchId}/objectives/{objectiveId}")
	public void unclaimObjective(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId,
									 @PathVariable String objectiveId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			ObjectiveDefinition objective = objectiveDefinitionRepo.getById(objectiveId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			objectivesService.unclaimObjective(player, objective, match);
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
			return allObjectivesService.getAllSecretObjectives(match, player).stream()
					.map(secretObjective -> new SecretObjectiveResponse(secretObjective, objectiveDefinitionRepo.getById(secretObjective.getObjective()).orElse(NULL_OBJECTIVE), match.getStartEra()
					))
					.sorted(SECRET_OBJECTIVE_SORT)
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
			MatchWithPlayers match = matchService.getById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			Optional<SecretObjective> chosenObjective = secretObjectives
					.stream().filter(o -> o.getObjective().equals(objective))
					.findFirst();
			if (chosenObjective.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				SecretObjective secretObjective = chosenObjective.get();
				ObjectiveDefinition imperiumSecretObjective = objectiveDefinitionRepo.getById(secretObjective.getObjective())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


				if (!secretObjective.getSelected() && secretObjectives.stream().filter(SecretObjective::getSelected).count() == 2) {
					// already have two other objectives selected
				} else if (!secretObjective.getSelected() &&
						secretObjectives.stream()
								.filter(SecretObjective::getSelected)
								.map(obj -> objectiveDefinitionRepo.getById(obj.getObjective()).orElse(NULL_OBJECTIVE))
								.filter(obj -> obj.getStars(match.getStartEra()) == 1)
								.count() == 1 &&
						imperiumSecretObjective.getStars(match.getStartEra()) == 1) {
					// Selecting a 1 star objective when 1 is already selected, which is not allowed
				} else {
					secretObjective.setSelected(!secretObjective.getSelected());
					matchService.updateSecretObjectiveSelection(secretObjective);
				}

				return secretObjectives.stream()
						.map(so -> new SecretObjectiveResponse(so, objectiveDefinitionRepo.getById(so.getObjective()).orElse(NULL_OBJECTIVE), match.getStartEra()
						))
						.collect(Collectors.toList());
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
					auditLogger.record(player, auditDescription.toString(), match);
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

	@PostMapping("/{matchId}/spectator")
	public void toggleSpectator(@RequestHeader("Authorization") String jwToken, @PathVariable int matchId) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (!player.getIsAdmin()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			} else {
				matchService.toggleSpectator(matchId);
			}
		}
	}

	@PutMapping("/{matchId}/{matchState}")
	@Transactional
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
					Match result = matchService.switchState(match.get(), matchState, payload, player);
					auditLogger.record(player, "Changed state of match " + result.getMatchName() + " to " + result.getMatchState(), match.get());
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
