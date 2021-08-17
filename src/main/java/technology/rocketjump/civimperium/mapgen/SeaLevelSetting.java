package technology.rocketjump.civimperium.mapgen;

public enum SeaLevelSetting implements Weightable {

	High(4f),
	Low(4f),
	Standard(12f);

	private final float weighting;

	SeaLevelSetting(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
