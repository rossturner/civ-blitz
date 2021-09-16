package technology.rocketjump.civblitz.model;

public class DecoratedMatch extends MatchWithPlayers {

	private int numTurns;
	private String startEraHint;

	public DecoratedMatch(MatchWithPlayers match) {
		super(match, match.signups);

		this.numTurns = match.getStartEra().numTurns;
		this.startEraHint = match.getStartEra().hint;
	}

	public int getNumTurns() {
		return numTurns;
	}

	public String getStartEraHint() {
		return startEraHint;
	}
}
