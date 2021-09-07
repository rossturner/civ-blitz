package technology.rocketjump.civimperium.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class ModifiersCsvParser {

	private final SourceDataRepo sourceDataRepe;

	@Autowired
	public ModifiersCsvParser(SourceDataRepo sourceDataRepe) {
		this.sourceDataRepe = sourceDataRepe;
	}

	public void parse(String traitModifiersCsv) throws IOException {
		try (Reader input = new StringReader(traitModifiersCsv)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String traitType = record.get("TraitType");
				String modifierId = record.get("ModifierId");

				Card card = sourceDataRepe.getByTraitType(traitType);
				if (card != null) {
					card.getModifierIds().add(modifierId);
				}
			}
		}
	}
}
