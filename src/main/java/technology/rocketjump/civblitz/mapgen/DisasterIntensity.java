package technology.rocketjump.civblitz.mapgen;

public enum DisasterIntensity implements Weightable {

	One(1, 1f),
	Two(2, 2f),
	Three(3, 2f),
	Four(4, 1f);

	public final int value;
	private final float weighting;

	DisasterIntensity(int value, float weighting) {
		this.value = value;
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
