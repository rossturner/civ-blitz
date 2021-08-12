package technology.rocketjump.civimperium.model;

import technology.rocketjump.civimperium.codegen.tables.pojos.MatchSignup;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;

public class MatchSignupWithPlayer extends MatchSignup {


	private final Player player;

	public MatchSignupWithPlayer(MatchSignup matchSignup, Player player) {
		super(matchSignup);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
