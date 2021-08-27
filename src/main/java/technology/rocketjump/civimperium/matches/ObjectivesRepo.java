package technology.rocketjump.civimperium.matches;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.*;
import technology.rocketjump.civimperium.codegen.tables.records.SecretObjectiveRecord;
import technology.rocketjump.civimperium.model.MatchWithPlayers;

import java.util.List;
import java.util.stream.Collectors;

import static technology.rocketjump.civimperium.codegen.Tables.*;

@Component
public class ObjectivesRepo {

	private final DSLContext create;

	@Autowired
	public ObjectivesRepo(DSLContext create) {
		this.create = create;
	}


	public void clear(Match match) {
		create.deleteFrom(SECRET_OBJECTIVE)
				.where(SECRET_OBJECTIVE.MATCH_ID.eq(match.getMatchId()))
				.execute();
		create.deleteFrom(PUBLIC_OBJECTIVE)
				.where(PUBLIC_OBJECTIVE.MATCH_ID.eq(match.getMatchId()))
				.execute();
	}

	public SecretObjective add(Match match, MatchSignup signup, ImperiumObjective objective) {
		SecretObjective secretObjective = new SecretObjective();
		secretObjective.setMatchId(match.getMatchId());
		secretObjective.setPlayerId(signup.getPlayerId());
		secretObjective.setObjective(objective);
		secretObjective.setSelected(false);
		secretObjective.setClaimed(false);
		SecretObjectiveRecord secretObjectiveRecord = create.newRecord(SECRET_OBJECTIVE, secretObjective);
		secretObjectiveRecord.store();
		return secretObjective;
	}

	public List<SecretObjective> getSecretObjectives(int matchId, Player player) {
		return create.selectFrom(SECRET_OBJECTIVE)
				.where(SECRET_OBJECTIVE.MATCH_ID.eq(matchId))
				.and(SECRET_OBJECTIVE.PLAYER_ID.eq(player.getPlayerId()))
				.fetchInto(SecretObjective.class);
	}

	public List<SecretObjective> getAllSecretObjectives(int matchId) {
		return create.selectFrom(SECRET_OBJECTIVE)
				.where(SECRET_OBJECTIVE.MATCH_ID.eq(matchId))
				.fetchInto(SecretObjective.class);
	}

	public List<PublicObjectiveWithClaimants> getAllPublicObjectives(int matchId) {
		List<PublicObjective> publicObjectives = create.selectFrom(PUBLIC_OBJECTIVE)
				.where(PUBLIC_OBJECTIVE.MATCH_ID.eq(matchId))
				.fetchInto(PublicObjective.class);

		List<ScoredObjective> scoredObjectives = create.selectFrom(SCORED_OBJECTIVE)
				.where(SCORED_OBJECTIVE.MATCH_ID.eq(matchId))
				.fetchInto(ScoredObjective.class);

		return publicObjectives.stream().map(p ->
				new PublicObjectiveWithClaimants(p, scoredObjectives.stream().filter(o -> o.getObjective().equals(p.getObjective())).map(ScoredObjective::getPlayerId).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	public void update(SecretObjective secretObjective) {
		create.update(SECRET_OBJECTIVE)
				.set(SECRET_OBJECTIVE.SELECTED, secretObjective.getSelected())
				.set(SECRET_OBJECTIVE.CLAIMED, secretObjective.getClaimed())
				.where(SECRET_OBJECTIVE.MATCH_ID.eq(secretObjective.getMatchId()))
				.and(SECRET_OBJECTIVE.PLAYER_ID.eq(secretObjective.getPlayerId()))
				.and(SECRET_OBJECTIVE.OBJECTIVE.eq(secretObjective.getObjective()))
				.execute();
	}

	public void addPublicObjectives(Match match, List<ImperiumObjective> publicObjectives) {
		for (ImperiumObjective objective : publicObjectives) {
			create.insertInto(PUBLIC_OBJECTIVE,
					PUBLIC_OBJECTIVE.MATCH_ID, PUBLIC_OBJECTIVE.OBJECTIVE)
					.values(match.getMatchId(), objective)
					.execute();
		}
	}

	public List<ScoredObjective> getAllClaims(Match match) {
		return create.selectFrom(SCORED_OBJECTIVE)
				.where(SCORED_OBJECTIVE.MATCH_ID.eq(match.getMatchId()))
				.fetchInto(ScoredObjective.class);
	}

	public void createClaim(ImperiumObjective objective, Player player, Match match) {
		ScoredObjective scoredObjective = new ScoredObjective();
		scoredObjective.setObjective(objective);
		scoredObjective.setPlayerId(player.getPlayerId());
		scoredObjective.setMatchId(match.getMatchId());
		create.newRecord(SCORED_OBJECTIVE, scoredObjective).store();
	}

	public void deleteClaim(ImperiumObjective objective, Player player, MatchWithPlayers match) {
		create.deleteFrom(SCORED_OBJECTIVE)
				.where(SCORED_OBJECTIVE.MATCH_ID.eq(match.getMatchId())
						.and(SCORED_OBJECTIVE.PLAYER_ID.eq(player.getPlayerId()))
						.and(SCORED_OBJECTIVE.OBJECTIVE.eq(objective)))
				.execute();
	}

}
