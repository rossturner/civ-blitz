package technology.rocketjump.civblitz.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;

class OAuth2UserAgentUtils {

	private static final String DISCORD_BOT_USER_AGENT = "CivBlitz (https://github.com/rossturner/civ-blitz)";

	static RequestEntity<?> withUserAgent(RequestEntity<?> request) {
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(request.getHeaders());
		headers.add(HttpHeaders.USER_AGENT, DISCORD_BOT_USER_AGENT);

		return new RequestEntity<>(request.getBody(), headers, request.getMethod(), request.getUrl());
	}

}