package technology.rocketjump.civimperium.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Component
public class LeaderTraitsParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public LeaderTraitsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceName) throws IOException {
		try (FileReader input = new FileReader(ResourceUtils.getFile("classpath:"+resourceName))) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civilizationType = record.get("CivilizationType");
				String civFriendlyName = getCivNameFrom(record.get("Name"));

				sourceDataRepo.addCivFriendlyName(civilizationType, civFriendlyName);

				Card card = new Card();
				card.setCivilizationType(civilizationType);
				card.setLeaderType(Optional.of(record.get("LeaderType")));
				card.setTraitType(record.get("TraitType"));
				card.setCardCategory(CardCategory.LeaderAbility);
				card.setCivilizationFriendlyName(civFriendlyName);
				card.setCardName(getLeaderNameFrom(record.get("Name")));
				card.setCardDescription(record.get("Description"));

				String grants = record.get("Grants");
				if (!grants.isEmpty()) {
					card.setGrantsTraitType(Optional.of(grants));
				}

				sourceDataRepo.add(card);
			}
		}
	}

	private String getCivNameFrom(String nameField) {
		return nameField.substring(0, nameField.indexOf('(') - 1);
	}

	private String getLeaderNameFrom(String nameField) {
		return nameField.substring(nameField.indexOf('(') + 1, nameField.indexOf(')'));
	}

}
