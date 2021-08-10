package technology.rocketjump.civimperium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.modgenerator.CompleteModGenerator;
import technology.rocketjump.civimperium.modgenerator.ModHeaderGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mods")
public class ModsController {

	private final SourceDataRepo sourceDataRepo;
	private final CompleteModGenerator completeModGenerator;
	private final ModHeaderGenerator modHeaderGenerator;

	@Autowired
	public ModsController(SourceDataRepo sourceDataRepo, CompleteModGenerator completeModGenerator, ModHeaderGenerator modHeaderGenerator) {
		this.sourceDataRepo = sourceDataRepo;
		this.completeModGenerator = completeModGenerator;
		this.modHeaderGenerator = modHeaderGenerator;
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

		String modName = modHeaderGenerator.createFor(selectedCards, startBiasCivType).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"Imperium_"+modName+".zip\"");

		return completeModGenerator.generateMod(selectedCards, startBiasCivType);
	}

}
