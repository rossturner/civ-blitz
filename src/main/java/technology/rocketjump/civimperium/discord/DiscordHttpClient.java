package technology.rocketjump.civimperium.discord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.logging.Logger;

@Component
public class DiscordHttpClient {

	private static Logger LOGGER = Logger.getLogger("technology.rocketjump.civimperium.discord.DiscordHttpClient");

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String discordClientId;
	private final String discordClientSecret;
	private final String getTokenUrl = "https://discordapp.com/api/v8/oauth2/token";
	private final String getCurrentUserUrl = "https://discordapp.com/api/users/@me";

	@Autowired
	public DiscordHttpClient(Environment env) {
		this.discordClientId = env.getProperty("spring.security.oauth2.client.registration.discord.client-id");
		this.discordClientSecret = env.getProperty("spring.security.oauth2.client.registration.discord.client-secret");
	}

	public DiscordAccessToken getToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", discordClientId);
		map.add("client_secret", discordClientSecret);
		map.add("grant_type", "authorization_code");
		map.add("code", code);


		String currentRequestUri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		String redirectUri = currentRequestUri.substring(0, currentRequestUri.indexOf("?"));
		LOGGER.info("Sending POST to " + getTokenUrl + " with redirectUri " + redirectUri);
		map.add("redirect_uri", redirectUri);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(getTokenUrl, request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return objectMapper.readValue(response.getBody(), DiscordAccessToken.class);
		} else {
			throw new ResponseStatusException(response.getStatusCode(), response.getBody());
		}
	}

	public DiscordUserInfo getCurrentUserInfo(DiscordAccessToken accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken.getAccess_token());
		HttpEntity<?> entity = new HttpEntity<>(headers);;

		ResponseEntity<DiscordUserInfo> response = restTemplate.exchange(getCurrentUserUrl, HttpMethod.GET, entity, DiscordUserInfo.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new ResponseStatusException(response.getStatusCode(), response.toString());
		}
	}

}
