package technology.rocketjump.civimperium.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.auth.JwtService;
import technology.rocketjump.civimperium.discord.DiscordAccessToken;
import technology.rocketjump.civimperium.discord.DiscordHttpClient;
import technology.rocketjump.civimperium.discord.DiscordUserInfo;

import static technology.rocketjump.civimperium.auth.SecurityConfiguration.isProduction;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	private final Environment environment;
	private final DiscordHttpClient discordHttpClient;
	private final JwtService jwtService;

	@Autowired
	public LoginController(Environment environment, DiscordHttpClient discordHttpClient, JwtService jwtService) {
		this.environment = environment;
		this.discordHttpClient = discordHttpClient;
		this.jwtService = jwtService;
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
		String jwt = jwtService.create(token, discordUser);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "/?token="+jwt);

		return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
	}

}
