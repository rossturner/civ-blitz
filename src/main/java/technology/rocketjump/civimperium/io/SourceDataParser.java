package technology.rocketjump.civimperium.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;

@Component
public class SourceDataParser {

	@Autowired
	public SourceDataParser(LeaderTraitsParser leaderTraitsParser, CivTraitsParser civTraitsParser,
							SourceDataRepo sourceDataRepo) throws IOException {
		leaderTraitsParser.parse("csv/LeaderTraits.csv");
		civTraitsParser.parse("csv/CivTraits.csv");

		sourceDataRepo.removeGrantedCards();
		System.out.println("Cards parsed: " + sourceDataRepo.getAll().size());
	}
}
