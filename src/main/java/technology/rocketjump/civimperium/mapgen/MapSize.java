package technology.rocketjump.civimperium.mapgen;

public enum MapSize {

	Duel(2, 4, 3, 6),
	Tiny(4, 6, 6, 10),
	Small(6, 10, 9, 14),
	Standard(8, 14, 12, 18),
	Large(10, 16, 15, 22),
	Huge(12, 20, 18, 24);

	public final int defaultPlayers;
	public final int maxPlayers;
	public final int defaultCityStates;
	public final int maxCityStates;

	private MapSize smaller, bigger;
	static {
		Duel.smaller = Duel;
		Duel.bigger = Tiny;

		Tiny.smaller = Duel;
		Tiny.bigger = Small;

		Small.smaller = Tiny;
		Small.bigger = Standard;

		Standard.smaller = Small;
		Standard.bigger = Large;

		Large.smaller = Standard;
		Large.bigger = Huge;

		Huge.smaller = Large;
		Huge.bigger = Huge;
	}

	MapSize(int defaultPlayers, int maxPlayers, int defaultCityStates, int maxCityStates) {
		this.defaultPlayers = defaultPlayers;
		this.maxPlayers = maxPlayers;
		this.defaultCityStates = defaultCityStates;
		this.maxCityStates = maxCityStates;
	}

	public MapSize getSmaller() {
		return smaller;
	}

	public MapSize getBigger() {
		return bigger;
	}
}
