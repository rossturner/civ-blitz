package technology.rocketjump.civimperium.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;

@Component
public class SourceDataParser {

	@Autowired
	public SourceDataParser(LeaderTraitsParser leaderTraitsParser, CivTraitsParser civTraitsParser,
							SubtypesParser subtypesParser, SourceDataRepo sourceDataRepo,
							@Qualifier("leaderTraits") String leaderTraitsContent,
							@Qualifier("civTraits") String civTraitsContent,
							@Qualifier("subtypes") String subtypesContent) throws IOException {
		leaderTraitsParser.parse(leaderTraitsContent);
		civTraitsParser.parse(civTraitsContent);
		subtypesParser.parse(subtypesContent);

		sourceDataRepo.removeGrantedCards();
		System.out.println("Cards parsed: " + sourceDataRepo.getAll().size());

		for (Card card : sourceDataRepo.getAll()) {
			if (card.getCardCategory().equals(CardCategory.UniqueUnit) || card.getCardCategory().equals(CardCategory.UniqueInfrastructure)) {
				if (card.getSubtype() == null) {
					throw new RuntimeException("No subtype for " + card.getTraitType());
				}

				if (card.getGrantsTraitType().isPresent()) {
					String grantedSubtype = subtypesParser.getSubtypeByTraitType(card.getGrantsTraitType().get());
					if (grantedSubtype == null) {
						throw new RuntimeException("No subtype for " + card.getGrantsTraitType().get());
					}
				}
			}
		}


	}

}
