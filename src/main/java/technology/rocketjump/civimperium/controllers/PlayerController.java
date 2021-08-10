package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technology.rocketjump.civimperium.cards.CollectionService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.CollectionCard;
import technology.rocketjump.civimperium.players.PlayerService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

	private final PlayerService playerService;
	private final CollectionService collectionService;

	@Autowired
	public PlayerController(PlayerService playerService, CollectionService collectionService) {
		this.playerService = playerService;
		this.collectionService = collectionService;
	}

	@GetMapping
	public ResponseEntity<Player> getLoggedInPlayer(Principal principal) {
		if (principal == null) {
			return ResponseEntity.noContent().build();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			Player player = getPlayer((OAuth2AuthenticationToken) principal);
			return ResponseEntity.ok(player);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/collection")
	public ResponseEntity<List<CollectionCard>> getPlayerCollection(Principal principal) {
		if (principal == null) {
			return ResponseEntity.notFound().build();
		} else {
			Player player = getPlayer((OAuth2AuthenticationToken) principal);
			List<CollectionCard> collection = collectionService.getCollection(player);
			return ResponseEntity.ok(collection);
		}
	}

	@GetMapping("/mulligan")
	public ResponseEntity<Boolean> canPlayerMulligan(Principal principal) {
		if (principal == null) {
			return ResponseEntity.notFound().build();
		} else {
			Player player = getPlayer((OAuth2AuthenticationToken) principal);
			if (collectionService.getTimesMulliganed(player) < CollectionService.MAX_MULLIGANS_ALLOWED) {
				return ResponseEntity.ok(true);
			} else {
				return ResponseEntity.ok(false);
			}
		}
	}

	@PutMapping("/mulligan")
	public ResponseEntity<List<CollectionCard>> triggerMulligan(Principal principal) {
		if (principal == null) {
			return ResponseEntity.notFound().build();
		} else {
			Player player = getPlayer((OAuth2AuthenticationToken) principal);
			collectionService.initialiseCollection(player, collectionService.getTimesMulliganed(player) + 1);
			List<CollectionCard> collection = collectionService.getCollection(player);
			return ResponseEntity.ok(collection);
		}
	}

	private Player getPlayer(OAuth2AuthenticationToken principal) {
		OAuth2AuthenticationToken token = principal;
		String discordId = token.getPrincipal().getAttribute("id");
		String discordUsername = token.getPrincipal().getAttribute("username");
		Player player = playerService.getPlayer(discordId, discordUsername);
		return player;
	}

}
