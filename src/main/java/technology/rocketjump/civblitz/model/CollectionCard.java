package technology.rocketjump.civblitz.model;

/**
 * Simple POJO to extend Card with a quantity for representing a player's collection
 */
public class CollectionCard extends Card {

	private int quantity;
	private Card freeUseCard;

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

	public Card getFreeUseCard() {
		return freeUseCard;
	}

	public void setFreeUseCard(Card freeUseCard) {
		this.freeUseCard = freeUseCard;
	}
}
