package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.matches.MatchService;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final MatchService matchService;

	@Autowired
	public MatchesController(JwtService jwtService, PlayerService playerService, MatchService matchService) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.matchService = matchService;
	}

	@PostMapping
	public ResponseEntity<Match> createMatch(@RequestHeader("Authorization") String jwToken,
											 @RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token.getDiscordId(), token.getDiscordUsername());
			if (!player.getIsAdmin()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			} else if (!payload.containsKey("timeslot")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			} else {
				String timeslot = payload.get("timeslot").toString();
				return ResponseEntity.ok(matchService.create(timeslot));
			}
		}
	}

}
