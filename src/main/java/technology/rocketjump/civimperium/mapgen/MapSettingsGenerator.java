package technology.rocketjump.civimperium.mapgen;

import org.springframework.stereotype.Component;

import java.util.Random;

import static technology.rocketjump.civimperium.mapgen.MapSize.*;

@Component
public class MapSettingsGenerator {

	private final Random random = new Random();

	public MapSettings generate(int numPlayers) {
		MapType mapType = selectRandomly(MapType.values());

		MapSize mapSize;
		if (mapType.getForcesMapSize() != null) {
			// Map size is forced, and sets most other settings too
			mapSize = mapType.getForcesMapSize();
			if (mapSize.defaultPlayers < numPlayers) {
				// Can't play on this map size with this many players, try again
				return generate(numPlayers);
			} else {
				return new MapSettings(StartEra.Ancient, mapType, mapSize, WorldAge.Standard, SeaLevelSetting.Standard,
						TemperatureSetting.Standard, RainfallSetting.Standard, mapSize.defaultCityStates, selectRandomly(DisasterIntensity.values()).value);
			}
		} else {
			StartEra startEraSetting = selectRandomly(StartEra.values());
			mapSize = pickSmallestPossibleMapSize(numPlayers);
//			mapSize = pickMapSize(numPlayers);
			SeaLevelSetting seaLevelSetting = selectRandomly(SeaLevelSetting.values());


//			if (lightOnLand(mapType, seaLevelSetting)) {
//				mapSize = mapSize.getBigger();
//			} else if (heavyOnLand(mapType, seaLevelSetting)) {
//				mapSize = mapSize.getSmaller();
//			}
//
//			while (mapSize.maxPlayers < numPlayers) {
//				mapSize = mapSize.getBigger();
//			}

			int numCityStates = mapSize.defaultCityStates - 2 + random.nextInt(5);
			while (numCityStates > mapSize.maxCityStates) {
				numCityStates--;
			}

			return new MapSettings(
					startEraSetting,
					mapType,
					mapSize,
					selectRandomly(WorldAge.values()),
					seaLevelSetting,
					selectRandomly(TemperatureSetting.values()),
					selectRandomly(RainfallSetting.values()),
					numCityStates,
					selectRandomly(DisasterIntensity.values()).value
			);
		}
	}

	private MapSize pickSmallestPossibleMapSize(int numPlayers) {
		MapSize mapSize = Duel;
		while (mapSize.maxPlayers < numPlayers) {
			mapSize = mapSize.getBigger();
		}
		return mapSize;
	}

	private boolean heavyOnLand(MapType mapType, SeaLevelSetting seaLevelSetting) {
		return mapType.landAmount.equals(MapType.MapLandAmount.VeryHeavy) ||
				(mapType.landAmount.equals(MapType.MapLandAmount.Heavy) && !seaLevelSetting.equals(SeaLevelSetting.High)) ||
				(mapType.landAmount.equals(MapType.MapLandAmount.Average) && seaLevelSetting.equals(SeaLevelSetting.Low));
	}

	private boolean lightOnLand(MapType mapType, SeaLevelSetting seaLevelSetting) {
		return mapType.landAmount.equals(MapType.MapLandAmount.Light);
	}

	private MapSize pickMapSize(int numPlayers) {
		switch (numPlayers) {
			case 2:
				return Duel;
			case 3:
				return random.nextBoolean() ? Duel : Tiny;
			case 4:
				return random.nextBoolean() ? Tiny : Small;
			case 5:
			case 6:
			case 7:
				return random.nextBoolean() ? Small : Standard;
			case 8:
			case 9:
			case 10:
			case 11:
				return random.nextBoolean() ? Standard : Large;
			default:
				return random.nextBoolean() ? Large : Huge;
		}
	}

	private <T extends Weightable> T selectRandomly(T[] instances) {
		float totalWeighting = 0f;
		for (T instance : instances) {
			totalWeighting += instance.getWeighting();
		}

		float roll = random.nextFloat() * totalWeighting;

		for (T instance : instances) {
			roll -= instance.getWeighting();
			if (roll <= 0) {
				return instance;
			}
		}

		throw new RuntimeException("Failed to randomly select a " + instances.getClass().getSimpleName());
	}
}
