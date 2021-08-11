package technology.rocketjump.civimperium.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.discord.DiscordAccessToken;
import technology.rocketjump.civimperium.discord.DiscordHttpClient;
import technology.rocketjump.civimperium.discord.DiscordUserInfo;
import technology.rocketjump.civimperium.players.PlayerService;

import static technology.rocketjump.civimperium.auth.SecurityConfiguration.isProduction;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	private final Environment environment;
	private final DiscordHttpClient discordHttpClient;
	private final JwtService jwtService;
	private final PlayerService playerService;

	@Autowired
	public LoginController(Environment environment, DiscordHttpClient discordHttpClient, JwtService jwtService, PlayerService playerService) {
		this.environment = environment;
		this.discordHttpClient = discordHttpClient;
		this.jwtService = jwtService;
		this.playerService = playerService;
	}


	@GetMapping("/discord")
	public ResponseEntity<String> discordOauthCode(@RequestParam(name = "code") String oauthCode,
												   @RequestParam(name = "state") String stateParam,
												   @CookieValue("state") String stateCookie) throws JsonProcessingException {
		if (isProduction(environment)) {
			if (!stateParam.equals(stateCookie)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("State sent to Discord does not match cookie");
			}
		}

		DiscordAccessToken token = discordHttpClient.getToken(oauthCode);
		DiscordUserInfo discordUser = discordHttpClient.getCurrentUserInfo(token);
		Player player = playerService.getPlayer(discordUser.getId(), discordUser.getUsername());
		String jwt = jwtService.create(token, player);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "/?token="+jwt);

		return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
	}

}
