package technology.rocketjump.civblitz.matches.guilds;

import java.util.Objects;

public class GuildDefinition {

	public static final GuildDefinition NULL_GUILD = new GuildDefinition("Unknown", "UNKNOWN",
			"This guild is missing", "Unknown", false);

	public final String guildId;
	public final String guildName;
	public final String description;

	public final String category;
	public final boolean active;

	public GuildDefinition(String guildName, String guildId, String description, String category, boolean active) {
		this.guildName = guildName;
		this.guildId = guildId;
		this.description = description;
		this.category = category;
		this.active = active;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuildDefinition that = (GuildDefinition) o;
		return guildId.equals(that.guildId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(guildId);
	}
}
