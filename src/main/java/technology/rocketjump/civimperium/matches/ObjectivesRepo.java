package technology.rocketjump.civimperium.matches;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.*;
import technology.rocketjump.civimperium.codegen.tables.records.SecretObjectiveRecord;

import java.util.List;

import static technology.rocketjump.civimperium.codegen.Tables.PUBLIC_OBJECTIVE;
import static technology.rocketjump.civimperium.codegen.Tables.SECRET_OBJECTIVE;

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

	public List<PublicObjective> getAllPublicObjectives(int matchId) {
		return create.selectFrom(PUBLIC_OBJECTIVE)
				.where(PUBLIC_OBJECTIVE.MATCH_ID.eq(matchId))
				.fetchInto(PublicObjective.class);
	}

	public void update(SecretObjective secretObjective) {
		create.update(SECRET_OBJECTIVE)
				.set(SECRET_OBJECTIVE.SELECTED, secretObjective.getSelected())
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
}
