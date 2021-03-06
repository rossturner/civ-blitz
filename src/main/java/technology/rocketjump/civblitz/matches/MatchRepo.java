package technology.rocketjump.civblitz.matches;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.codegen.tables.pojos.Match;
import technology.rocketjump.civblitz.codegen.tables.pojos.MatchSignup;
import technology.rocketjump.civblitz.codegen.tables.pojos.MatchSignupCard;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;
import technology.rocketjump.civblitz.codegen.tables.records.MatchRecord;
import technology.rocketjump.civblitz.codegen.tables.records.MatchSignupCardRecord;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.MatchSignupWithPlayer;
import technology.rocketjump.civblitz.model.MatchWithPlayers;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.util.*;
import java.util.stream.Collectors;

import static technology.rocketjump.civblitz.codegen.tables.Match.MATCH;
import static technology.rocketjump.civblitz.codegen.tables.MatchSignup.MATCH_SIGNUP;
import static technology.rocketjump.civblitz.codegen.tables.MatchSignupCard.MATCH_SIGNUP_CARD;
import static technology.rocketjump.civblitz.codegen.tables.Player.PLAYER;
import static technology.rocketjump.civblitz.matches.MatchState.SIGNUPS;

@Component
public class MatchRepo {

	private final DSLContext create;
	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public MatchRepo(DSLContext create, SourceDataRepo sourceDataRepo) {
		this.create = create;
		this.sourceDataRepo = sourceDataRepo;
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
								.map(signup -> {
									Player player = playersById.get(signup.getPlayerId());
									List<Card> selectedCards = getSelectedCards(signup);
									return new MatchSignupWithPlayer(signup, player, selectedCards);
								})
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
				.orderBy(MATCH.MATCH_ID.desc())
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
								.map(signup -> new MatchSignupWithPlayer(signup, playersById.get(signup.getPlayerId()),
										getSelectedCards(signup)))
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
				.set(MATCH.SPECTATOR, match.getSpectator())
				.where(MATCH.MATCH_ID.eq(match.getMatchId()))
				.execute();
	}

	public void updateSignup(MatchSignup matchSignup) {
		create.update(MATCH_SIGNUP)
				.set(MATCH_SIGNUP.START_BIAS_CIV_TYPE, matchSignup.getStartBiasCivType())
				.set(MATCH_SIGNUP.COMMITTED, matchSignup.getCommitted())
				.set(MATCH_SIGNUP.FINAL_POINTS_AWARDED, matchSignup.getFinalPointsAwarded())
				.where(MATCH_SIGNUP.MATCH_ID.eq(matchSignup.getMatchId())
						.and(MATCH_SIGNUP.PLAYER_ID.eq(matchSignup.getPlayerId())))
				.execute();
	}

	public void delete(MatchWithPlayers match) {
		create.deleteFrom(MATCH_SIGNUP)
				.where(MATCH_SIGNUP.MATCH_ID.eq(match.getMatchId()))
				.execute();

		create.deleteFrom(MATCH)
				.where(MATCH.MATCH_ID.eq(match.getMatchId()))
				.execute();
	}

	private List<Card> getSelectedCards(MatchSignup signup) {
		return create.select(MATCH_SIGNUP_CARD.CARD_IDENTIFIER)
				.from(MATCH_SIGNUP_CARD)
				.where(MATCH_SIGNUP_CARD.MATCH_ID.eq(signup.getMatchId()).and(MATCH_SIGNUP_CARD.PLAYER_ID.eq(signup.getPlayerId())))
				.fetch(MATCH_SIGNUP_CARD.CARD_IDENTIFIER)
				.stream().map(sourceDataRepo::getByIdentifier)
				.collect(Collectors.toList());
	}

	public List<MatchSignup> getSignupsForPlayer(Player player) {
		return create.selectFrom(MATCH_SIGNUP)
				.where(MATCH_SIGNUP.PLAYER_ID.eq(player.getPlayerId()))
				.fetchInto(MatchSignup.class);
	}

	public void addCardSelection(MatchSignupWithPlayer matchSignup, Card card, boolean isFree) {
		MatchSignupCard cardSignup = new MatchSignupCard();
		cardSignup.setMatchId(matchSignup.getMatchId());
		cardSignup.setPlayerId(matchSignup.getPlayerId());
		cardSignup.setCardIdentifier(card.getIdentifier());
		cardSignup.setIsFree(isFree);

		MatchSignupCardRecord cardSignupRecord = create.newRecord(MATCH_SIGNUP_CARD, cardSignup);
		cardSignupRecord.store();
		matchSignup.getSelectedCards().add(new Card(card));
	}

	public Optional<MatchSignupCard> removeCardSelection(MatchSignupWithPlayer matchSignup, Card card) {
		Optional<MatchSignupCardRecord> optRecord = create.selectFrom(MATCH_SIGNUP_CARD)
				.where(MATCH_SIGNUP_CARD.MATCH_ID.eq(matchSignup.getMatchId())
						.and(MATCH_SIGNUP_CARD.PLAYER_ID.eq(matchSignup.getPlayerId()))
						.and(MATCH_SIGNUP_CARD.CARD_IDENTIFIER.eq(card.getIdentifier())))
				.fetchOptional();

		if (optRecord.isPresent()) {
			MatchSignupCard pojo = optRecord.get().into(MatchSignupCard.class);
			optRecord.get().delete();
			matchSignup.getSelectedCards().remove(card);
			return Optional.of(pojo);
		} else {
			return Optional.empty();
		}
	}

	public boolean isFreeUse(MatchSignup matchSignup, Card card) {
		Optional<MatchSignupCard> matchSignupCard = create.selectFrom(MATCH_SIGNUP_CARD)
				.where(MATCH_SIGNUP_CARD.MATCH_ID.eq(matchSignup.getMatchId())
						.and(MATCH_SIGNUP_CARD.PLAYER_ID.eq(matchSignup.getPlayerId()))
						.and(MATCH_SIGNUP_CARD.CARD_IDENTIFIER.eq(card.getIdentifier())))
				.fetchOptionalInto(MatchSignupCard.class);

		if (matchSignupCard.isPresent()) {
			return matchSignupCard.get().getIsFree();
		} else {
			return false;
		}
	}
}
