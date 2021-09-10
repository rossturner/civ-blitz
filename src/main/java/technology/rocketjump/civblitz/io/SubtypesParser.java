package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class SubtypesParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public SubtypesParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;

	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String traitType = record.get("TraitType");
				String subtype = record.get("Type");

				sourceDataRepo.addSubtypeByTraitType(traitType, subtype);

				Card card = sourceDataRepo.getByTraitType(traitType);
				if (card == null) {
					throw new RuntimeException("Could not find card for TraitType " + traitType);
				} else {
					card.setSubtype(subtype);
				}
			}
		}
	}

}
