package technology.rocketjump.civimperium.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.IconAtlasEntry;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;

@Component
public class SourceDataParser {

	@Autowired
	public SourceDataParser(LeaderTraitsParser leaderTraitsParser, CivTraitsParser civTraitsParser,
							SubtypesParser subtypesParser, SourceDataRepo sourceDataRepo, IconParser iconParser,
							PlayersParser playersParser,
							@Qualifier("leaderTraits") String leaderTraitsContent,
							@Qualifier("civTraits") String civTraitsContent,
							@Qualifier("subtypes") String subtypesContent,
							@Qualifier("CivIcons") String civIconsContent,
							@Qualifier("LeaderIcons") String leaderIconsContent,
							@Qualifier("Players") String playersContent) throws IOException {
		leaderTraitsParser.parse(leaderTraitsContent);
		civTraitsParser.parse(civTraitsContent);
		subtypesParser.parse(subtypesContent);
		iconParser.parse(civIconsContent);
		iconParser.parse(leaderIconsContent);
		playersParser.parse(playersContent);

		sourceDataRepo.removeGrantedCards();
		System.out.println("Cards parsed: " + sourceDataRepo.getAll().size());

		verifyIconAtlasEntries(sourceDataRepo);
	}

	private void verifyIconAtlasEntries(SourceDataRepo sourceDataRepo) {
		for (Card card : sourceDataRepo.getAll()) {
			if (card.getCardCategory().equals(CardCategory.LeaderAbility)) {
				IconAtlasEntry iconAtlasEntry = sourceDataRepo.getIconAtlasEntry(card.getLeaderType().get());
				if (iconAtlasEntry == null) {
					throw new RuntimeException("Could not find icon atlas entry for " + card.getLeaderType().get());
				}
			} else if (card.getCardCategory().equals(CardCategory.CivilizationAbility)) {
				IconAtlasEntry iconAtlasEntry = sourceDataRepo.getIconAtlasEntry(card.getCivilizationType());
				if (iconAtlasEntry == null) {
					throw new RuntimeException("Could not find icon atlas entry for " + card.getCivilizationType());
				}
			}
		}
	}

}
