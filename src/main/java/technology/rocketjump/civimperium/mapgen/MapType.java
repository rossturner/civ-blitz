package technology.rocketjump.civimperium.mapgen;

import static technology.rocketjump.civimperium.mapgen.MapType.MapLandAmount.*;

public enum MapType implements Weightable {

	Archipelago(1f, Light),
	Continents(8f, Average),
	Continents_and_Islands(6f, Average),
	Fractal(1f, Average),
	Highlands(3f, VeryHeavy),
	Island_Plates(4f, Light),
	Lakes(2f, VeryHeavy),
	Pangaea(8f, Heavy),
	Primordial(3f, Heavy),

	// Real world maps, split between a weighting of 1
	// Note that these maps have a max players of map size default players
	Earth(0.25f, Average),
	East_Asia(0.25f, Average),
	Europe(0.25f, Heavy),
	Mediterranean_Large(0.25f, Heavy),

	Seven_Seas(5f, Heavy),
	Shuffle(2f, Average),
	Small_Continents(2f, Average),
	Terra(2f, Light),
	Wetlands(2f, Average);

	public final float weighting;
	public final MapLandAmount landAmount;
	private MapSize forcesMapSize;
	static {
		Earth.forcesMapSize = MapSize.Standard;
		East_Asia.forcesMapSize = MapSize.Standard;
		Europe.forcesMapSize = MapSize.Standard;
		Mediterranean_Large.forcesMapSize = MapSize.Large;
	}

	MapType(float weighting, MapLandAmount landAmount) {
		this.weighting = weighting;
		this.landAmount = landAmount;
	}

	public MapSize getForcesMapSize() {
		return forcesMapSize;
	}

	@Override
	public float getWeighting() {
		return weighting;
	}

	public enum MapLandAmount {

		Light,
		Average,
		Heavy,
		VeryHeavy

	}
}
