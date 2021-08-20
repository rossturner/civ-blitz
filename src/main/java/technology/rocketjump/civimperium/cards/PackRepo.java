package technology.rocketjump.civimperium.cards;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.CardPack;

import static technology.rocketjump.civimperium.codegen.tables.CardPack.CARD_PACK;

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
}
