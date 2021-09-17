package technology.rocketjump.civblitz.players;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static technology.rocketjump.civblitz.codegen.tables.Player.PLAYER;

@Component
public class PlayerRepo {

	private final DSLContext create;

	private static final List<String> adminDiscordIds = Arrays.asList(
			"291857466491273218", // Zsinj
			"149222835850706944" // Harringzord
	);

	@Autowired
	public PlayerRepo(DSLContext create) {
		this.create = create;
	}

	public Optional<Player> getPlayerByDiscordId(String discordId) {
		return create.selectFrom(PLAYER).where(PLAYER.PLAYER_ID.eq(discordId)).fetchOptionalInto(Player.class);
	}

	public Player createPlayer(String discordId, String discordUsername, String discordAvatar) {
		Player newPlayer = new Player();
		newPlayer.setPlayerId(discordId);
		newPlayer.setDiscordUsername(discordUsername);
		newPlayer.setDiscordAvatar(discordAvatar);
		newPlayer.setBalance(0.0);
		newPlayer.setRankingScore(0.0);
		newPlayer.setTotalPointsEarned(0.0);
		if (adminDiscordIds.contains(discordId)) {
			newPlayer.setIsAdmin(true);
		} else {
			newPlayer.setIsAdmin(false);
		}
		create.newRecord(PLAYER, newPlayer).store();
		return newPlayer;
	}

	public void updateAvatar(String discordId, String discordAvatar) {
		create.update(PLAYER).set(PLAYER.DISCORD_AVATAR, discordAvatar).where(PLAYER.PLAYER_ID.eq(discordId)).execute();
	}

	public void updateBalances(Player player) {
		create.update(PLAYER)
				.set(PLAYER.BALANCE, player.getBalance())
				.set(PLAYER.TOTAL_POINTS_EARNED, player.getTotalPointsEarned())
				.set(PLAYER.RANKING_SCORE, player.getRankingScore())
				.where(PLAYER.PLAYER_ID.eq(player.getPlayerId()))
				.execute();
	}

	public List<Player> getPlayersByRankingScore() {
		return create.selectFrom(PLAYER)
				.where(PLAYER.RANKING_SCORE.greaterThan(0.0))
				.orderBy(PLAYER.RANKING_SCORE.desc())
				.limit(100)
				.fetchInto(Player.class);
	}
}
