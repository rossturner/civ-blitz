package technology.rocketjump.civimperium.mapgen;

public enum WorldAge implements Weightable {

	New(4f),
	Old(4f),
	Standard(12f);

	private final float weighting;

	WorldAge(float weighting) {
		this.weighting = weighting;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}
}
