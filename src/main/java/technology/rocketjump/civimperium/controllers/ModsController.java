package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import technology.rocketjump.civimperium.matches.MatchRepo;
import technology.rocketjump.civimperium.model.*;
import technology.rocketjump.civimperium.modgenerator.CompleteModGenerator;
import technology.rocketjump.civimperium.modgenerator.ModHeaderGenerator;
import technology.rocketjump.civimperium.modgenerator.model.ModdedCivInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public byte[] getMod(@RequestParam(name="traitType") List<String> traitTypes,
						 @RequestParam(name="startBias") String startBiasCivType,
						 HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
		response.setStatus(HttpServletResponse.SC_OK);
		Map<CardCategory, Card> selectedCards = new HashMap<>();
		traitTypes.stream().map(sourceDataRepo::getByTraitType).forEach(card -> {
			selectedCards.put(card.getCardCategory(), card);
		});

		String modName = modHeaderGenerator.createFor(selectedCards).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"Imperium_"+modName+".zip\"");

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
			Map<CardCategory, Card> selectedCards = new HashMap<>();
			for (CardCategory category : CardCategory.values()) {
				Card card = sourceDataRepo.getByTraitType(signup.getCard(category));
				selectedCards.put(card.getCardCategory(), card);
			}
			civs.add(new ModdedCivInfo(selectedCards, signup.getStartBiasCivType()));
		}

		String modName = modHeaderGenerator.createFor(match.getMatchName()).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"Imperium_"+modName+".zip\"");

		return completeModGenerator.generateMod(match.getMatchName(), civs);
	}

}
