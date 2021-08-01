package technology.rocketjump.civimperium.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;
import technology.rocketjump.civimperium.modgenerator.CompleteModGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/example")
public class ExampleController {

	private final SourceDataRepo sourceDataRepo;
	private final CompleteModGenerator completeModGenerator;

	@Autowired
	public ExampleController(SourceDataRepo sourceDataRepo, CompleteModGenerator completeModGenerator) {
		this.sourceDataRepo = sourceDataRepo;
		this.completeModGenerator = completeModGenerator;
	}

	@GetMapping("/cards")
	public Collection<Card> getAllCards() {
		return sourceDataRepo.getAll();
	}

	@GetMapping("/mod")
	public String getSomething() {
		Map<CardCategory, Card> selectedCards = new HashMap<>();
		for (Card card : List.of(
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_ADJACENT_DISTRICTS"),
				sourceDataRepo.getByTraitType("TRAIT_LEADER_ANTIQUES_AND_PARKS"),
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_BUILDING_MADRASA"),
				sourceDataRepo.getByTraitType("TRAIT_CIVILIZATION_UNIT_CREE_OKIHTCITAW")
		)) {
			selectedCards.put(card.getCardCategory(), card);
		}

		return completeModGenerator.generateMod(selectedCards);
	}

}
