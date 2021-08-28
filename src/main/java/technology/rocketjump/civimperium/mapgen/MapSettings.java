package technology.rocketjump.civimperium.mapgen;

public class MapSettings {

	public final StartEra startEra;
	public final MapType mapType;
	public final MapSize mapSize;
	public final WorldAge worldAge;
	public final SeaLevelSetting seaLevel;
	public final TemperatureSetting temperature;
	public final RainfallSetting rainfall;
	public final int numCityStates;
	public final int disasterIntensity;

	public MapSettings(StartEra startEra, MapType mapType, MapSize mapSize, WorldAge worldAge, SeaLevelSetting seaLevel,
					   TemperatureSetting temperature, RainfallSetting rainfall, int numCityStates, int disasterIntensity) {
		this.startEra = startEra;
		this.mapType = mapType;
		this.mapSize = mapSize;
		this.worldAge = worldAge;
		this.seaLevel = seaLevel;
		this.temperature = temperature;
		this.rainfall = rainfall;
		this.numCityStates = numCityStates;
		this.disasterIntensity = disasterIntensity;
	}
}
