package technology.rocketjump.civblitz.cards;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;
import technology.rocketjump.civblitz.codegen.tables.pojos.PlayerCollection;
import technology.rocketjump.civblitz.codegen.tables.records.PlayerCollectionRecord;
import technology.rocketjump.civblitz.model.Card;

import java.util.List;
import java.util.Optional;

import static technology.rocketjump.civblitz.codegen.tables.PlayerCollection.PLAYER_COLLECTION;

@Component
public class CollectionRepo {

	private final DSLContext create;

	@Autowired
	public CollectionRepo(DSLContext create) {
		this.create = create;
	}

	public void deleteCollection(Player player) {
		create.deleteFrom(PLAYER_COLLECTION).where(PLAYER_COLLECTION.PLAYER_ID.eq(player.getPlayerId())).execute();
	}

	public List<PlayerCollection> getCollection(Player player) {
		return create.selectFrom(PLAYER_COLLECTION).where(PLAYER_COLLECTION.PLAYER_ID.eq(player.getPlayerId())).fetchInto(PlayerCollection.class);
	}

	public void addToCollection(Card card, Player player) {
		Optional<PlayerCollectionRecord> existingEntry = create.selectFrom(PLAYER_COLLECTION)
				.where(PLAYER_COLLECTION.PLAYER_ID.eq(player.getPlayerId())
						.and(PLAYER_COLLECTION.CARD_IDENTIFIER.eq(card.getIdentifier())))
				.fetchOptional();

		if (existingEntry.isPresent()) {
			PlayerCollectionRecord record = existingEntry.get();
			record.setQuantity(record.getQuantity() + 1);
			record.store();
		} else {
			PlayerCollection collectionEntry = new PlayerCollection();
			collectionEntry.setPlayerId(player.getPlayerId());
			collectionEntry.setCardIdentifier(card.getIdentifier());
			collectionEntry.setQuantity(1);
			create.newRecord(PLAYER_COLLECTION, collectionEntry).store();
		}
	}

	public void removeFromCollection(Card card, Player player) {
		Optional<PlayerCollectionRecord> existingEntry = create.selectFrom(PLAYER_COLLECTION)
				.where(PLAYER_COLLECTION.PLAYER_ID.eq(player.getPlayerId())
						.and(PLAYER_COLLECTION.CARD_IDENTIFIER.eq(card.getIdentifier())))
				.fetchOptional();

		if (existingEntry.isPresent()) {
			PlayerCollectionRecord collectionRecord = existingEntry.get();
			collectionRecord.setQuantity(collectionRecord.getQuantity() - 1);
			if (collectionRecord.getQuantity() == 0) {
				collectionRecord.delete();
			} else {
				collectionRecord.store();
			}
		}
	}
}
