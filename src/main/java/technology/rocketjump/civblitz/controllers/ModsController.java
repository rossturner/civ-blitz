package technology.rocketjump.civblitz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civblitz.matches.MatchRepo;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.MatchSignupWithPlayer;
import technology.rocketjump.civblitz.model.MatchWithPlayers;
import technology.rocketjump.civblitz.model.SourceDataRepo;
import technology.rocketjump.civblitz.modgenerator.CompleteModGenerator;
import technology.rocketjump.civblitz.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mods")
public class ModsController {

	private final SourceDataRepo sourceDataRepo;
	private final CompleteModGenerator completeModGenerator;
	private final ModHeaderGenerator modHeaderGenerator;
	private final MatchRepo matchRepo;

	@Autowired
	public ModsController(SourceDataRepo sourceDataRepo, CompleteModGenerator completeModGenerator,
						  ModHeaderGenerator modHeaderGenerator, MatchRepo matchRepo) {
		this.sourceDataRepo = sourceDataRepo;
		this.completeModGenerator = completeModGenerator;
		this.modHeaderGenerator = modHeaderGenerator;
		this.matchRepo = matchRepo;
	}

	@GetMapping(produces = "application/zip")
	@ResponseBody
	// TODO pass in identifiers rather than traitType
	public byte[] getMod(@RequestParam(name= "cardIdentifier") List<String> cardIdentifiers,
						 @RequestParam(name="startBias") String startBiasCivType,
						 HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
		response.setStatus(HttpServletResponse.SC_OK);
		List<Card> selectedCards = cardIdentifiers.stream().map(sourceDataRepo::getByIdentifier).collect(Collectors.toList());

		String modName = modHeaderGenerator.createFor(selectedCards).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"CivBlitz_"+modName+".zip\"");

		return completeModGenerator.generateMod(new ModdedCivInfo(selectedCards, startBiasCivType));
	}

	@GetMapping(value = "/matches/{matchId}", produces = "application/zip")
	@ResponseBody
	public byte[] getModForMatch(@PathVariable int matchId,
						 HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
		response.setStatus(HttpServletResponse.SC_OK);

		MatchWithPlayers match = matchRepo.getMatchById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		List<ModdedCivInfo> civs = new ArrayList<>();
		for (MatchSignupWithPlayer signup : match.signups) {
			civs.add(new ModdedCivInfo(signup.getSelectedCards(), signup.getStartBiasCivType()));
		}

		String modName = modHeaderGenerator.createFor(match.getMatchName()).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"CivBlitz_"+modName+".zip\"");

		return completeModGenerator.generateMod(match.getMatchName(), civs);
	}

}
