package technology.rocketjump.civimperium.cards;

public enum CardPackType {

	SINGLE_CARD(2),
	MULTIPLE_CARDS(5),
	MATCH_BOOSTER(5);

	public final int cost;

	CardPackType(int cost) {
		this.cost = cost;
	}
}
