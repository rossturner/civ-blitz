package technology.rocketjump.civblitz.cards;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.codegen.tables.pojos.CardPack;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;

import java.util.List;
import java.util.Optional;

import static technology.rocketjump.civblitz.codegen.tables.CardPack.CARD_PACK;

@Component
public class PackRepo {

	private final DSLContext create;

	@Autowired
	public PackRepo(DSLContext create) {
		this.create = create;
	}

	public void create(CardPack pack) {
		create.newRecord(CARD_PACK, pack).store();
	}

	public List<CardPack> getAllForPlayer(Player player) {
		return create.selectFrom(CARD_PACK)
				.where(CARD_PACK.PLAYER_ID.eq(player.getPlayerId()))
				.fetchInto(CardPack.class);
	}

	public Optional<CardPack> get(Integer packId) {
		return create.selectFrom(CARD_PACK)
				.where(CARD_PACK.PACK_ID.eq(packId))
				.fetchOptionalInto(CardPack.class);
	}

	public void delete(CardPack pack) {
		create.deleteFrom(CARD_PACK)
				.where(CARD_PACK.PACK_ID.eq(pack.getPackId()))
				.execute();
	}
}
