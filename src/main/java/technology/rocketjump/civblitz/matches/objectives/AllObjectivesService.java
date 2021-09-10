package technology.rocketjump.civblitz.matches.objectives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;
import technology.rocketjump.civblitz.codegen.tables.pojos.SecretObjective;
import technology.rocketjump.civblitz.matches.MatchState;
import technology.rocketjump.civblitz.matches.guilds.GuildDefinition;
import technology.rocketjump.civblitz.matches.guilds.GuildDefinitionRepo;
import technology.rocketjump.civblitz.matches.guilds.GuildRepo;
import technology.rocketjump.civblitz.model.MatchWithPlayers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static technology.rocketjump.civblitz.matches.MatchState.IN_PROGRESS;

@Service
public class AllObjectivesService {

	private final ObjectivesRepo objectivesRepo;
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;
	private final GuildRepo guildRepo;
	private final GuildDefinitionRepo guildDefinitionRepo;

	@Autowired
	public AllObjectivesService(ObjectivesRepo objectivesRepo, ObjectiveDefinitionRepo objectiveDefinitionRepo,
								GuildRepo guildRepo, GuildDefinitionRepo guildDefinitionRepo) {
		this.objectivesRepo = objectivesRepo;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
		this.guildRepo = guildRepo;
		this.guildDefinitionRepo = guildDefinitionRepo;
	}

	public List<PublicObjectiveWithClaimants> getPublicObjectives(int matchId) {
		return objectivesRepo.getAllPublicObjectives(matchId);
	}

	public List<SecretObjective> getAllSecretObjectives(MatchWithPlayers match, Player player) {
		if (match.getMatchState().equals(IN_PROGRESS)) {
			boolean playerIsAdminAndNotInMatch = player.getIsAdmin() &&
					!match.signups.stream().anyMatch(s -> s.getPlayer().equals(player));

			return objectivesRepo.getAllSecretObjectives(match.getMatchId()).stream()
					.map(secretObjective -> {
						if (isObjectiveVisible(secretObjective, player, playerIsAdminAndNotInMatch)) {
							return secretObjective;
						} else {
							Optional<ObjectiveDefinition> definition = objectiveDefinitionRepo.getById(secretObjective.getObjective());
							if (definition.isEmpty()) {
								return null;
							} else {
								Integer numStars = definition.get().getStars(match.getStartEra());
								SecretObjective hidden = new SecretObjective(secretObjective);
								hidden.setObjective("HIDDEN_"+numStars);
								return hidden;
							}
						}
					})
					.collect(Collectors.toList());
		} else if (match.getMatchState().equals(MatchState.POST_MATCH) || match.getMatchState().equals(MatchState.COMPLETED)) {
			// At this state anyone can get all secret objectives
			return objectivesRepo.getAllSecretObjectives(match.getMatchId());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not retrieve all secret objectives in state " + match.getMatchState());
		}
	}

	private boolean isObjectiveVisible(SecretObjective s, Player player, boolean playerIsAdminAndNotInMatch) {
		return s.getClaimed() || s.getPlayerId().equals(player.getPlayerId()) || playerIsAdminAndNotInMatch;
	}

	public List<GuildDefinition> getMatchGuilds(int matchId) {
		return guildRepo.getMatchGuilds(matchId)
				.stream()
				.map(matchGuild -> guildDefinitionRepo.getById(matchGuild.getGuildId()).orElse(GuildDefinition.NULL_GUILD))
				.collect(Collectors.toList());
	}
}
