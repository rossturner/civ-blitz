package technology.rocketjump.civimperium.mapgen;

public enum RainfallSetting implements Weightable {

	Arid(4f),
	Wet(4f),
	Standard(12f);

	private final float weighting;

	RainfallSetting(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
