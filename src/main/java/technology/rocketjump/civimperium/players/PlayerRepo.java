package technology.rocketjump.civimperium.players;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;

import java.util.Optional;

import static technology.rocketjump.civimperium.codegen.tables.Player.PLAYER;

@Component
public class PlayerRepo {

	private final DSLContext create;

	@Autowired
	public PlayerRepo(DSLContext create) {
		this.create = create;
	}

	public Optional<Player> getPlayerByDiscordId(String discordId) {
		return create.selectFrom(PLAYER).where(PLAYER.PLAYER_ID.eq(discordId)).fetchOptionalInto(Player.class);
	}

	public Player createPlayer(String discordId, String discordUsername) {
		Player newPlayer = new Player();
		newPlayer.setPlayerId(discordId);
		newPlayer.setDiscordUsername(discordUsername);
		newPlayer.setBalance(0.0);
		newPlayer.setRankingScore(0.0);
		newPlayer.setTotalPointsEarned(0.0);
		create.newRecord(PLAYER, newPlayer).store();
		return newPlayer;
	}
}