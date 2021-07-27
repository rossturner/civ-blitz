package technology.rocketjump.civimperium.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/example")
public class ExampleController {

	@Autowired
	public ExampleController() {

	}

	@GetMapping("/data")
	public List<String> getSomeText() {
		return List.of("A", "B", "C");
	}

}
