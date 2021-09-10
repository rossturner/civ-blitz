package technology.rocketjump.civblitz.auth;

public class CivBlitzToken {

	private String discordId;
	private String discordUsername;
	private String discordAvatar;
	private String discordAccessToken;
	private String discordRefreshToken;

	public String getDiscordId() {
		return discordId;
	}

	public void setDiscordId(String discordId) {
		this.discordId = discordId;
	}

	public String getDiscordUsername() {
		return discordUsername;
	}

	public void setDiscordUsername(String discordUsername) {
		this.discordUsername = discordUsername;
	}

	public String getDiscordAccessToken() {
		return discordAccessToken;
	}

	public void setDiscordAccessToken(String discordAccessToken) {
		this.discordAccessToken = discordAccessToken;
	}

	public String getDiscordRefreshToken() {
		return discordRefreshToken;
	}

	public void setDiscordRefreshToken(String discordRefreshToken) {
		this.discordRefreshToken = discordRefreshToken;
	}

	public String getDiscordAvatar() {
		return discordAvatar;
	}

	public void setDiscordAvatar(String discordAvatar) {
		this.discordAvatar = discordAvatar;
	}
}
