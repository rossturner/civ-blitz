package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.cards.CollectionService;
import technology.rocketjump.civimperium.cards.PackDetails;
import technology.rocketjump.civimperium.cards.PackService;
import technology.rocketjump.civimperium.codegen.tables.pojos.CardPack;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CollectionCard;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

	private final JwtService jwtService;
	private final PlayerService playerService;
	private final CollectionService collectionService;
	private final PackService packService;

	@Autowired
	public PlayerController(JwtService jwtService, PlayerService playerService, CollectionService collectionService,
							PackService packService) {
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.collectionService = collectionService;
		this.packService = packService;
	}

	@GetMapping
	public ResponseEntity<Player> getLoggedInPlayer(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.noContent().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			return ResponseEntity.ok(player);
		}
	}

	@GetMapping("/collection")
	public ResponseEntity<List<CollectionCard>> getPlayerCollection(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			List<CollectionCard> collection = collectionService.getCollection(player);
			return ResponseEntity.ok(collection);
		}
	}

	@GetMapping("/mulligan")
	public ResponseEntity<Boolean> canPlayerMulligan(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			if (collectionService.getTimesMulliganed(player) < CollectionService.MAX_MULLIGANS_ALLOWED) {
				return ResponseEntity.ok(true);
			} else {
				return ResponseEntity.ok(false);
			}
		}
	}

	@PutMapping("/mulligan")
	public ResponseEntity<List<CollectionCard>> triggerMulligan(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			collectionService.initialiseCollection(player, collectionService.getTimesMulliganed(player) + 1);
			List<CollectionCard> collection = collectionService.getCollection(player);
			return ResponseEntity.ok(collection);
		}
	}


	@GetMapping("/packs")
	public ResponseEntity<List<CardPack>> getPlayerPacks(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);
			List<CardPack> packs = packService.getAllPacks(player);
			return ResponseEntity.ok(packs);
		}
	}

	@PostMapping("/packs")
	public ResponseEntity<PackDetails> openNextPack(@RequestHeader("Authorization") String jwToken) {
		if (jwToken == null) {
			return ResponseEntity.notFound().build();
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);

			List<CardPack> allPacks = packService.getAllPacks(player);
			if (allPacks.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} else {
				CardPack nextPack = allPacks.get(0);
				List<Card> cards = packService.openPack(nextPack, player);
				return ResponseEntity.ok(new PackDetails(nextPack, cards));
			}

		}
	}

}
