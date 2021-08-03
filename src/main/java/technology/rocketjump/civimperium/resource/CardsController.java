package technology.rocketjump.civimperium.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.util.Collection;

@RestController
@RequestMapping("/api/cards")
public class CardsController {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CardsController(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@GetMapping
	public Collection<Card> getAllCards() {
		return sourceDataRepo.getAll();
	}

}
