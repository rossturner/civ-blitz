package technology.rocketjump.civimperium.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.discord.DiscordAccessToken;
import technology.rocketjump.civimperium.discord.DiscordUserInfo;

import java.util.Date;

@Service
public class JwtService {

	private final Algorithm algorithm;

	public JwtService(Environment env) {
		// Re-using discord client secret for secret key
		String secret = env.getProperty("spring.security.oauth2.client.registration.discord.client-secret");
		if (secret == null) {
			throw new IllegalArgumentException("spring.security.oauth2.client.registration.discord.client-secret must be set");
		}
		this.algorithm = Algorithm.HMAC256(secret);
	}

	public String create(DiscordAccessToken accessToken, DiscordUserInfo userInfo) {
		return JWT.create()
				.withExpiresAt(new Date(accessToken.getExpires_at() * 1000L))
				.withSubject(userInfo.getId())
				.withClaim("username", userInfo.getUsername())
				.withClaim("access_token", accessToken.getAccess_token())
				.withClaim("refresh_token", accessToken.getRefresh_token())
				.sign(algorithm);
	}

	public ImperiumToken parse(String jsonWebToken) {
		DecodedJWT decoded = JWT.decode(jsonWebToken);
		ImperiumToken imperiumToken = new ImperiumToken();
		imperiumToken.setDiscordId(decoded.getSubject());
		imperiumToken.setDiscordUsername(decoded.getClaim("username").asString());
		imperiumToken.setDiscordAccessToken(decoded.getClaim("access_token").asString());
		imperiumToken.setDiscordRefreshToken(decoded.getClaim("refresh_token").asString());
		return imperiumToken;
	}

}
