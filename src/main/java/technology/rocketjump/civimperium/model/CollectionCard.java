package technology.rocketjump.civimperium.model;

/**
 * Simple POJO to extend Card with a quantity for representing a player's collection
 */
public class CollectionCard extends Card {

	private int quantity;

	public CollectionCard(Card original, int quantity) {
		super(original);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
