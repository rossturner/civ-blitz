package technology.rocketjump.civimperium.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.modgenerator.CompleteModGenerator;
import technology.rocketjump.civimperium.modgenerator.ModHeaderGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/example")
public class ExampleController {

	private final SourceDataRepo sourceDataRepo;
	private final CompleteModGenerator completeModGenerator;
	private final ModHeaderGenerator modHeaderGenerator;

	@Autowired
	public ExampleController(SourceDataRepo sourceDataRepo, CompleteModGenerator completeModGenerator, ModHeaderGenerator modHeaderGenerator) {
		this.sourceDataRepo = sourceDataRepo;
		this.completeModGenerator = completeModGenerator;
		this.modHeaderGenerator = modHeaderGenerator;
	}

	@GetMapping("/cards")
	public Collection<Card> getAllCards() {
		return sourceDataRepo.getAll();
	}

	@GetMapping(value = "/mod", produces = "application/zip")
	@ResponseBody
	public byte[] getSomething(HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
		response.setStatus(HttpServletResponse.SC_OK);
		Map<CardCategory, Card> selectedCards = new HashMap<>();
		for (Card card : List.of(
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_MAYAB"),
				sourceDataRepo.getByTraitType("TRAIT_LEADER_KUPES_VOYAGE"),
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_DISTRICT_SEOWON"),
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_UNIT_AZTEC_EAGLE_WARRIOR")
		)) {
			selectedCards.put(card.getCardCategory(), card);
		}

		String modName = modHeaderGenerator.createFor(selectedCards).modName;
		response.addHeader("Content-Disposition", "attachment; filename=\"Imperium_"+modName+".zip\"");

		return completeModGenerator.generateMod(selectedCards);
	}

}
