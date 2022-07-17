package technology.rocketjump.civblitz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CardRarity {

	// Currently 9/24 = 37.5% chance to get non-common rarity
	Common(15),
	Uncommon(4),
	Rare(3),
	Epic(2);

	private final int weighting;

	private static final List<CardRarity> weightedList = new ArrayList<>();
	static {
		for (CardRarity rarity : CardRarity.values()) {
			for (int cursor = 0; cursor < rarity.weighting; cursor++) {
				weightedList.add(rarity);
			}
		}

	}

	CardRarity(int weighting) {
		this.weighting = weighting;
	}

	public static CardRarity pickRarityForPotentialUpgrade(Random random) {
		return weightedList.get(random.nextInt(weightedList.size()));
	}
}
