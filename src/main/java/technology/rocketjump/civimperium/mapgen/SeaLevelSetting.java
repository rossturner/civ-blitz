package technology.rocketjump.civimperium.mapgen;

public enum SeaLevelSetting implements Weightable {

	High(12f),
	Low(2f),
	Standard(4f);

	private final float weighting;

	SeaLevelSetting(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
