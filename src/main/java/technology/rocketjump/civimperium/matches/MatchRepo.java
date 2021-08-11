package technology.rocketjump.civimperium.matches;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.records.MatchRecord;

import java.util.Optional;

import static technology.rocketjump.civimperium.codegen.tables.Match.MATCH;
import static technology.rocketjump.civimperium.codegen.tables.MatchSignup.MATCH_SIGNUP;
import static technology.rocketjump.civimperium.matches.MatchState.SIGNUPS;

@Component
public class MatchRepo {

	private final DSLContext create;

	@Autowired
	public MatchRepo(DSLContext create) {
		this.create = create;
	}

	public Integer getNumActiveMatches(Player player) {
		return create.selectCount()
				.from(MATCH_SIGNUP.leftJoin(MATCH)
						.on(MATCH.MATCH_ID.eq(MATCH_SIGNUP.MATCH_ID)))
				.where(MATCH_SIGNUP.PLAYER_ID.eq(player.getPlayerId())
						.and(MATCH.MATCH_STATE.notEqual(SIGNUPS)))
				.fetchOne(0, Integer.class);
	}

	public Optional<Match> getMatchByName(String name) {
		return create.selectFrom(MATCH)
				.where(MATCH.MATCH_NAME.eq(name))
				.fetchOptionalInto(Match.class);
	}

	public Match createMatch(String matchName, String timeslot) {
		Match newMatch = new Match();
		newMatch.setMatchName(matchName);
		newMatch.setMatchState(SIGNUPS);
		newMatch.setTimeslot(timeslot);
		MatchRecord matchRecord = create.newRecord(MATCH, newMatch);
		matchRecord.store();
		newMatch.setMatchId(matchRecord.getMatchId());
		return newMatch;
	}
}
