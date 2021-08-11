package technology.rocketjump.civimperium.matches;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

@Component
public class AdjectiveNounNameGenerator {

	private final List<String> adjectives = new ArrayList<>();
	private final List<String> nouns = new ArrayList<>();

	@Autowired
	public AdjectiveNounNameGenerator(@Qualifier("adjectives") String adjectivesContent,
			@Qualifier("nouns") String nounsContent) {
		parseAdjectives(adjectivesContent);
		parseNouns(nounsContent);
	}

	public String generateName(Random random) {
		String adjective =adjectives.get(random.nextInt(adjectives.size()));
		String noun = nouns.get(random.nextInt(nouns.size()));
		return StringUtils.capitalize(adjective) + " " + noun;
	}

	private void parseAdjectives(String adjectivesContent) {
		StringTokenizer stringTokenizer = new StringTokenizer(adjectivesContent, "\n");
		while (stringTokenizer.hasMoreTokens()) {
			String line = stringTokenizer.nextToken();
			if (line.length() > 2) {
				if (line.endsWith("1") || line.endsWith("2") || line.endsWith("3")) {
					continue;
				}
				String adjective = line.split("\\s")[1];
				adjectives.add(adjective);
			}
		}
	}

	private void parseNouns(String nounsContent) {
		StringTokenizer stringTokenizer = new StringTokenizer(nounsContent);
		while (stringTokenizer.hasMoreTokens()) {
			String line = stringTokenizer.nextToken();
			if (!line.isEmpty()) {
				nouns.add(line);
			}
		}
	}
}
