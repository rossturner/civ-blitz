package technology.rocketjump.civimperium.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

import static technology.rocketjump.civimperium.io.MediaNameUtil.simplify;

@Component
public class LeaderTraitsParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public LeaderTraitsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civilizationType = record.get("CivilizationType");
				String civFriendlyName = getCivNameFrom(record.get("Name"));
				String traitType = record.get("TraitType");

				sourceDataRepo.addCivFriendlyName(civilizationType, civFriendlyName);

				Card card = new Card();
				card.setCivilizationType(civilizationType);
				card.setLeaderType(Optional.of(record.get("LeaderType")));
				card.setTraitType(traitType);
				card.setMediaName(simplify(record.get("MediaName")));
				card.setCardCategory(CardCategory.LeaderAbility);
				card.setCivilizationFriendlyName(civFriendlyName);
				card.setCardName(getLeaderNameFrom(record.get("Name")));
				card.setCardDescription(record.get("Description"));

				String grants = record.get("Grants");
				if (!grants.isEmpty()) {
					card.setGrantsTraitType(Optional.of(grants));
				}

				sourceDataRepo.add(card);

				String locTraitName = record.get("LocTraitTypeName");
				String locTraitDesc = record.get("LocTraitTypeDesc");
				if (locTraitDesc == null || locTraitDesc.isEmpty()) {
					locTraitDesc = locTraitName.replace("_NAME", "_DESCRIPTION");
				}
				sourceDataRepo.leaderTraitNameByTraitType.put(traitType, locTraitName);
				sourceDataRepo.leaderTraitDescByTraitType.put(traitType, locTraitDesc);
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
