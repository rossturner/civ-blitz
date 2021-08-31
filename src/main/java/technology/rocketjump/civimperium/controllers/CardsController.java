package technology.rocketjump.civimperium.controllers;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.cards.CardPackType;
import technology.rocketjump.civimperium.cards.PackService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.players.PlayerService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
public class CardsController {

	private final SourceDataRepo sourceDataRepo;
	private final JwtService jwtService;
	private final PlayerService playerService;
	private final PackService packService;

	@Autowired
	public CardsController(SourceDataRepo sourceDataRepo, JwtService jwtService, PlayerService playerService, PackService packService) {
		this.sourceDataRepo = sourceDataRepo;
		this.jwtService = jwtService;
		this.playerService = playerService;
		this.packService = packService;
	}

	@GetMapping
	public Collection<Card> getAllCards() {
		return sourceDataRepo.getAll();
	}

	@PostMapping("/purchase")
	@Transactional
	public void purchasePack(@RequestHeader("Authorization") String jwToken,
												   @RequestBody Map<String, Object> payload) {
		if (jwToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} else {
			ImperiumToken token = jwtService.parse(jwToken);
			Player player = playerService.getPlayer(token);

			CardPackType packType = EnumUtils.getEnum(CardPackType.class, payload.get("type").toString());
			if (packType == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown pack type");
			}
			String category = (String) payload.get("category");

			packService.purchasePack(player, packType, category);
		}
	}
}
