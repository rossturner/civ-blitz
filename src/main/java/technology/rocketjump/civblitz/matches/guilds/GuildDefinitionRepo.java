package technology.rocketjump.civblitz.matches.guilds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GuildDefinitionRepo {

	private final Map<String, GuildDefinition> byGuildId = new HashMap<>();

	@Autowired
	public GuildDefinitionRepo() {
	}

	public void add(GuildDefinition guildDefinition) {
		byGuildId.put(guildDefinition.guildId, guildDefinition);
	}

	public Optional<GuildDefinition> getById(String guildId) {
		return Optional.ofNullable(byGuildId.get(guildId));
	}

	public List<GuildDefinition> getAll() {
		return new ArrayList<>(byGuildId.values());
	}

}
