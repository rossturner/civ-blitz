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

	public Optional<MatchWithPlayers> getMatchById(int matchId) {
		Map<Match, List<MatchSignup>> results = create.select(MATCH.fields()).select(MATCH_SIGNUP.fields())
				.from(MATCH.leftJoin(MATCH_SIGNUP).on(MATCH.MATCH_ID.eq(MATCH_SIGNUP.MATCH_ID)))
				.where(MATCH.MATCH_ID.eq(matchId))
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
				.findFirst();
	}

	public Match createMatch(String matchName, String timeslot) {
		Match newMatch = new Match();
		newMatch.setMatchName(matchName);
		newMatch.setMatchState(SIGNUPS);
		newMatch.setSpectator(false);
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
			newSignup.setCivAbilityIsFree(false);
			newSignup.setLeaderAbilityIsFree(false);
			newSignup.setUniqueInfrastructureIsFree(false);
			newSignup.setUniqueUnitIsFree(false);
			create.newRecord(MATCH_SIGNUP, newSignup).store();
		}
	}

	public void resign(Match match, Player player) {
		create.deleteFrom(MATCH_SIGNUP)
				.where(MATCH_SIGNUP.MATCH_ID.eq(match.getMatchId()))
				.and(MATCH_SIGNUP.PLAYER_ID.eq(player.getPlayerId()))
				.execute();
	}

	public void update(Match match) {
		create.update(MATCH)
				.set(MATCH.MATCH_NAME, match.getMatchName())
				.set(MATCH.TIMESLOT, match.getTimeslot())
				.set(MATCH.MATCH_STATE, match.getMatchState())
				.set(MATCH.START_ERA, match.getStartEra())
				.set(MATCH.MAP_TYPE, match.getMapType())
				.set(MATCH.MAP_SIZE, match.getMapSize())
				.set(MATCH.WORLD_AGE, match.getWorldAge())
				.set(MATCH.SEA_LEVEL, match.getSeaLevel())
				.set(MATCH.TEMPERATURE, match.getTemperature())
				.set(MATCH.RAINFALL, match.getRainfall())
				.set(MATCH.CITY_STATES, match.getCityStates())
				.set(MATCH.DISASTER_INTENSITY, match.getDisasterIntensity())
				.where(MATCH.MATCH_ID.eq(match.getMatchId()))
				.execute();
	}

	public void updateSignup(MatchSignup matchSignup) {
		create.update(MATCH_SIGNUP)
				.set(MATCH_SIGNUP.CARD_CIV_ABILITY, matchSignup.getCardCivAbility())
				.set(MATCH_SIGNUP.CARD_LEADER_ABILITY, matchSignup.getCardLeaderAbility())
				.set(MATCH_SIGNUP.CARD_UNIQUE_INFRASTRUTURE, matchSignup.getCardUniqueInfrastruture())
				.set(MATCH_SIGNUP.CARD_UNIQUE_UNIT, matchSignup.getCardUniqueUnit())
				.set(MATCH_SIGNUP.CIV_ABILITY_IS_FREE, matchSignup.getCivAbilityIsFree())
				.set(MATCH_SIGNUP.LEADER_ABILITY_IS_FREE, matchSignup.getLeaderAbilityIsFree())
				.set(MATCH_SIGNUP.UNIQUE_INFRASTRUCTURE_IS_FREE, matchSignup.getUniqueInfrastructureIsFree())
				.set(MATCH_SIGNUP.UNIQUE_UNIT_IS_FREE, matchSignup.getUniqueUnitIsFree())
				.set(MATCH_SIGNUP.START_BIAS_CIV_TYPE, matchSignup.getStartBiasCivType())
				.set(MATCH_SIGNUP.COMMITTED, matchSignup.getCommitted())
				.where(MATCH_SIGNUP.MATCH_ID.eq(matchSignup.getMatchId())
						.and(MATCH_SIGNUP.PLAYER_ID.eq(matchSignup.getPlayerId())))
				.execute();
	}
}
