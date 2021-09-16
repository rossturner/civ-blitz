package technology.rocketjump.civblitz.mapgen;

public enum StartEra implements Weightable {

	Ancient(1f, "", 100),
	Medieval(1f, "", 80),
	Industrial(1f, "No Great Prophets are available with an Industrial start era.", 80);

	private final float weighting;
	public final String hint;
	public final int numTurns;

	StartEra(float weighting, String hint, int numTurns) {
		this.weighting = weighting;
		this.hint = hint;
		this.numTurns = numTurns;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}

}
