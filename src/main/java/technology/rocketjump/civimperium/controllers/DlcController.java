package technology.rocketjump.civimperium.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.cards.DlcService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.CollectionCard;
import technology.rocketjump.civimperium.model.PlayerDlcResponse;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/dlc")
public class DlcController {

	private final SourceDataRepo sourceDataRepo;
	private final JwtService jwtService;
	private final PlayerService playerService;
	private final DlcService dlcService;

	@Autowired
	public DlcController(SourceDataRepo sourceDataRepo, JwtService jwtService, PlayerService playerService, DlcService dlcService) {
		this.sourceDataRepo = sourceDataRepo;
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.dlcService = dlcService;
	}

	@GetMapping
	public List<PlayerDlcResponse> getPlayerDlcSettings(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);

			return dlcService.getPlayerSettings(player);
		}
	}

	@PostMapping
	public List<CollectionCard> dryRunDlcChange(@RequestHeader("Authorization") String jwToken, @RequestBody List<String> selectedDlcNames) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);

			return dlcService.getCardsToBeRerolled(player, selectedDlcNames);
		}
	}

	@PostMapping("/confirm")
	@Transactional
	public void updateDlcSettings(@RequestHeader("Authorization") String jwToken, @RequestBody List<String> selectedDlcNames) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);

			dlcService.updateSettings(player, selectedDlcNames);
		}
	}
}
