package technology.rocketjump.civblitz.matches.guilds;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.codegen.tables.pojos.Match;
import technology.rocketjump.civblitz.codegen.tables.pojos.MatchGuild;
import technology.rocketjump.civblitz.codegen.tables.records.MatchGuildRecord;

import java.util.List;

import static technology.rocketjump.civblitz.codegen.Tables.MATCH_GUILD;

@Component
public class GuildRepo {

	private final DSLContext create;

	@Autowired
	public GuildRepo(DSLContext create) {
		this.create = create;
	}


	public void clear(Match match) {
		create.deleteFrom(MATCH_GUILD)
				.where(MATCH_GUILD.MATCH_ID.eq(match.getMatchId()))
				.execute();
	}

	public MatchGuild add(Match match, GuildDefinition guildDefinition) {
		MatchGuild matchGuild = new MatchGuild();
		matchGuild.setMatchId(match.getMatchId());
		matchGuild.setGuildId(guildDefinition.guildId);
		MatchGuildRecord record = create.newRecord(MATCH_GUILD, matchGuild);
		record.store();
		return matchGuild;
	}

	public List<MatchGuild> getMatchGuilds(int matchId) {
		return create.selectFrom(MATCH_GUILD)
				.where(MATCH_GUILD.MATCH_ID.eq(matchId))
				.fetchInto(MatchGuild.class);
	}

}
