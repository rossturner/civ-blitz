package technology.rocketjump.civimperium.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.discord.DiscordAccessToken;
import technology.rocketjump.civimperium.discord.DiscordHttpClient;
import technology.rocketjump.civimperium.discord.DiscordUserInfo;

import static technology.rocketjump.civimperium.auth.SecurityConfiguration.isProduction;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	private final Environment environment;
	private final DiscordHttpClient discordHttpClient;

	@Autowired
	public LoginController(Environment environment, DiscordHttpClient discordHttpClient) {
		this.environment = environment;
		this.discordHttpClient = discordHttpClient;
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
		// Get discord user info

		// Set token and user info in JWT

		// set JWT as cookie

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "/");

		return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
	}

}
