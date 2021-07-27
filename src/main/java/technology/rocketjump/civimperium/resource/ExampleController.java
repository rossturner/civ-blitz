package technology.rocketjump.civimperium.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.util.Collection;

@RestController
@RequestMapping("/example")
public class ExampleController {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public ExampleController(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	@GetMapping("/cards")
	public Collection<Card> getAllCards() {
		return sourceDataRepo.getAll();
	}

}
