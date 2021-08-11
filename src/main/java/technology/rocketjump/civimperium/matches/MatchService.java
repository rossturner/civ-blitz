package technology.rocketjump.civimperium.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;

import java.util.Optional;
import java.util.Random;

@Service
public class MatchService {

	private final AdjectiveNounNameGenerator adjectiveNounNameGenerator;
	private final MatchRepo matchRepo;
	private Random random = new Random();

	@Autowired
	public MatchService(AdjectiveNounNameGenerator adjectiveNounNameGenerator, MatchRepo matchRepo) {
		this.adjectiveNounNameGenerator = adjectiveNounNameGenerator;
		this.matchRepo = matchRepo;
	}


	public Match create(String timeslot) {
		String matchName;
		Optional<Match> existingMatchWithSameName;
		do {
			matchName = adjectiveNounNameGenerator.generateName(random);
			existingMatchWithSameName = matchRepo.getMatchByName(matchName);
		} while (existingMatchWithSameName.isPresent());

		return matchRepo.createMatch(matchName, timeslot);
	}

}
