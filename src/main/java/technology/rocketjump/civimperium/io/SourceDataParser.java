package technology.rocketjump.civimperium.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;

@Component
public class SourceDataParser {

	@Autowired
	public SourceDataParser(LeaderTraitsParser leaderTraitsParser, CivTraitsParser civTraitsParser,
							SourceDataRepo sourceDataRepo,
							@Qualifier("leaderTraits") String leaderTraitsContent,
							@Qualifier("civTraits") String civTraitsContent) throws IOException {
		leaderTraitsParser.parse(leaderTraitsContent);
		civTraitsParser.parse(civTraitsContent);

		sourceDataRepo.removeGrantedCards();
		System.out.println("Cards parsed: " + sourceDataRepo.getAll().size());
	}

}
