package technology.rocketjump.civblitz.mapgen;

public enum StartEra implements Weightable {

	Ancient(4f),
	Medieval(2f),
	Industrial(0f);

	private final float weighting;

	StartEra(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}

}
