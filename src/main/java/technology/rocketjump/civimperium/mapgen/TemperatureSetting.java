package technology.rocketjump.civimperium.mapgen;

public enum TemperatureSetting implements Weightable {

	Hot(4f),
	Cold(4f),
	Standard(12f);

	private final float weighting;

	TemperatureSetting(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
