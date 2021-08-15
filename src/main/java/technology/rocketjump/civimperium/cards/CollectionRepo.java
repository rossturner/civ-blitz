package technology.rocketjump.civimperium.cards;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.Collection;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.records.CollectionRecord;
import technology.rocketjump.civimperium.model.Card;

import java.util.List;
import java.util.Optional;

import static technology.rocketjump.civimperium.codegen.tables.Collection.COLLECTION;

@Component
public class CollectionRepo {

	private final DSLContext create;

	@Autowired
	public CollectionRepo(DSLContext create) {
		this.create = create;
	}

	public void deleteCollection(Player player) {
		create.deleteFrom(COLLECTION).where(COLLECTION.PLAYER_ID.eq(player.getPlayerId())).execute();
	}

	public List<Collection> getCollection(Player player) {
		return create.selectFrom(COLLECTION).where(COLLECTION.PLAYER_ID.eq(player.getPlayerId())).fetchInto(Collection.class);
	}

	public void addToCollection(Card card, Player player) {
		Optional<CollectionRecord> existingEntry = create.selectFrom(COLLECTION)
				.where(COLLECTION.PLAYER_ID.eq(player.getPlayerId())
						.and(COLLECTION.CARD_TRAIT_TYPE.eq(card.getTraitType())))
				.fetchOptional();

		if (existingEntry.isPresent()) {
			CollectionRecord record = existingEntry.get();
			record.setQuantity(record.getQuantity() + 1);
			record.store();
		} else {
			Collection collectionEntry = new Collection();
			collectionEntry.setPlayerId(player.getPlayerId());
			collectionEntry.setCardTraitType(card.getTraitType());
			collectionEntry.setQuantity(1);
			create.newRecord(COLLECTION, collectionEntry).store();
		}
	}

	public void removeFromCollection(Card card, Player player) {
		Optional<CollectionRecord> existingEntry = create.selectFrom(COLLECTION)
				.where(COLLECTION.PLAYER_ID.eq(player.getPlayerId())
						.and(COLLECTION.CARD_TRAIT_TYPE.eq(card.getTraitType())))
				.fetchOptional();

		if (existingEntry.isPresent()) {
			CollectionRecord collectionRecord = existingEntry.get();
			collectionRecord.setQuantity(collectionRecord.getQuantity() - 1);
			if (collectionRecord.getQuantity() == 0) {
				collectionRecord.delete();
			} else {
				collectionRecord.store();
			}
		}
	}
}
