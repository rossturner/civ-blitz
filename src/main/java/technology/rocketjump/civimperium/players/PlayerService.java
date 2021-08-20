package technology.rocketjump.civimperium.players;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.auth.ImperiumToken;
import technology.rocketjump.civimperium.cards.CollectionService;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;

import java.util.Optional;

@Service
public class PlayerService {

	private final PlayerRepo playerRepo;
	private final CollectionService collectionService;

	@Autowired
	public PlayerService(PlayerRepo playerRepo, CollectionService collectionService) {
		this.playerRepo = playerRepo;
		this.collectionService = collectionService;
	}

	public Player getPlayer(ImperiumToken token) {
		return getPlayer(token.getDiscordId(), token.getDiscordUsername(), token.getDiscordAvatar());
	}

	public synchronized Player getPlayer(String discordId, String discordUsername, String discordAvatar) {
		Optional<Player> existingPlayer = playerRepo.getPlayerByDiscordId(discordId);
		if (existingPlayer.isPresent()) {
			if (existingPlayer.get().getDiscordAvatar() == null && discordAvatar != null) {
				playerRepo.updateAvatar(discordId, discordAvatar);
				existingPlayer.get().setDiscordAvatar(discordAvatar);
			}
			return existingPlayer.get();
		} else {
			Player player = playerRepo.createPlayer(discordId, discordUsername, discordAvatar);
			collectionService.initialiseCollection(player, 0);
			return player;
		}
	}

}
