package technology.rocketjump.civimperium.players;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	public Player getPlayer(String discordId, String discordUsername) {
		Optional<Player> existingPlayer = playerRepo.getPlayerByDiscordId(discordId);
		if (existingPlayer.isPresent()) {
			return existingPlayer.get();
		} else {
			Player player = playerRepo.createPlayer(discordId, discordUsername);
			collectionService.initialiseCollection(player, 0);
			return player;
		}
	}

}
