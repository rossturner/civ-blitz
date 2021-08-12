package technology.rocketjump.civimperium.matches;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.MatchSignup;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.records.MatchRecord;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.*;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.codegen.tables.Match.MATCH;
import static technology.rocketjump.civimperium.codegen.tables.MatchSignup.MATCH_SIGNUP;
import static technology.rocketjump.civimperium.codegen.tables.Player.PLAYER;
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

	public Optional<Match> getMatchById(int matchId) {
		return create.selectFrom(MATCH)
				.where(MATCH.MATCH_ID.eq(matchId))
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

	public List<MatchWithPlayers> getUncompletedMatches() {
		Map<Match, List<MatchSignup>> results = create.select(MATCH.fields()).select(MATCH_SIGNUP.fields())
				.from(MATCH.leftJoin(MATCH_SIGNUP).on(MATCH.MATCH_ID.eq(MATCH_SIGNUP.MATCH_ID)))
				.where(MATCH.MATCH_STATE.notEqual(MatchState.COMPLETED))
				.fetchGroups(
						r -> r.into(MATCH).into(Match.class),
						r -> r.into(MATCH_SIGNUP).into(MatchSignup.class)
				);

		Set<String> playerIds = new HashSet<>();
		results.values().stream().flatMap(Collection::stream)
				.forEach(signup -> {
					if (signup.getPlayerId() != null) {
						playerIds.add(signup.getPlayerId());
					}
				});

		Map<String, Player> playersById = new HashMap<>();
		create.selectFrom(PLAYER)
				.where(PLAYER.PLAYER_ID.in(playerIds))
				.fetchInto(Player.class)
				.forEach(player -> playersById.put(player.getPlayerId(), player));


		return results.entrySet()
				.stream().map(entry -> new MatchWithPlayers(entry.getKey(),
						entry.getValue().stream()
								.filter(signup -> signup.getPlayerId() != null)
								.map(signup -> new MatchSignupWithPlayer(signup, playersById.get(signup.getPlayerId())))
								.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	public void signup(Match match, Player player) {
		Optional<MatchSignup> existingSignup = create.selectFrom(MATCH_SIGNUP)
				.where(MATCH_SIGNUP.MATCH_ID.eq(match.getMatchId()))
				.and(MATCH_SIGNUP.PLAYER_ID.eq(player.getPlayerId()))
				.fetchOptionalInto(MatchSignup.class);

		if (existingSignup.isEmpty()) {
			MatchSignup newSignup = new MatchSignup();
			newSignup.setMatchId(match.getMatchId());
			newSignup.setPlayerId(player.getPlayerId());
			newSignup.setCommitted(false);
			newSignup.setCardBoosterClaimed(false);
			create.newRecord(MATCH_SIGNUP, newSignup).store();
		}
	}

	public void resign(Match match, Player player) {
		create.deleteFrom(MATCH_SIGNUP)
				.where(MATCH_SIGNUP.MATCH_ID.eq(match.getMatchId()))
				.and(MATCH_SIGNUP.PLAYER_ID.eq(player.getPlayerId()))
				.execute();
	}
}
