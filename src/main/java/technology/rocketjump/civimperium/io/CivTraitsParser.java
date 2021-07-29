package technology.rocketjump.civimperium.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

@Component
public class CivTraitsParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public CivTraitsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civilizationType = record.get("CivilizationType");
				String friendlyCivName = sourceDataRepo.getFriendlyCivName(civilizationType);

				Card card = new Card();
				card.setCivilizationType(civilizationType);
				card.setTraitType(record.get("TraitType"));
				card.setCardCategory(CardCategory.parseAcronym(record.get("CardType")));
				card.setCivilizationFriendlyName(friendlyCivName);

				String description = record.get("Description");
				if (card.getCardCategory().equals(CardCategory.CivilizationAbility)) {
					card.setCardName(friendlyCivName);
					card.setCardDescription(description);
				} else {
					card.setCardName(description.substring(0, description.indexOf(":")));
					card.setCardDescription(StringUtils.capitalize(description.substring(description.indexOf(":") + 2)));
				}

				String grants = record.get("Grants");
				if (!grants.isEmpty()) {
					card.setGrantsTraitType(Optional.of(grants));
				}

				sourceDataRepo.add(card);
			}
		}
	}

}
