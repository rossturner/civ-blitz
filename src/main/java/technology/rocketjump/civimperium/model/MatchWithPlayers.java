package technology.rocketjump.civimperium.model;

import technology.rocketjump.civimperium.codegen.tables.pojos.Match;

import java.util.List;

public class MatchWithPlayers extends Match {

	public final List<MatchSignupWithPlayer> signups;

	public MatchWithPlayers(Match match, List<MatchSignupWithPlayer> signups) {
		super(match);
		if (signups == null) {
			signups = List.of();
		}
		this.signups = signups;
	}

}
