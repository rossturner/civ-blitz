package technology.rocketjump.civimperium.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.CardPack;
import technology.rocketjump.civimperium.model.MatchSignupWithPlayer;

@Service
public class PackService {

	private final PackRepo packRepo;

	@Autowired
	public PackService(PackRepo packRepo) {
		this.packRepo = packRepo;
	}

	public void addMatchBooster(MatchSignupWithPlayer player) {
		CardPack pack = new CardPack();
		pack.setPlayerId(player.getPlayerId());
		pack.setPackType(CardPackType.MATCH_BOOSTER);

		pack.setNumCivAbility(player.getCivAbilityIsFree() ? 0 : 1);
		pack.setNumLeaderAbility(player.getLeaderAbilityIsFree() ? 0 : 1);
		pack.setNumUniqueInfrastructure(player.getUniqueInfrastructureIsFree() ? 0 : 1);
		pack.setNumUniqueUnit(player.getUniqueUnitIsFree() ? 0 : 1);

		packRepo.create(pack);
	}
}
