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
import java.util.HashMap;
import java.util.Map;

@Component
public class SubtypesParser {

	private final SourceDataRepo sourceDataRepo;
	private final Map<String, String> subtypesByTraitType = new HashMap<>();

	@Autowired
	public SubtypesParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;

	}

	public String getSubtypeByTraitType(String traitType) {
		return subtypesByTraitType.get(traitType);
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String traitType = record.get("TraitType");
				String subtype = record.get("Type");

				subtypesByTraitType.put(traitType, subtype);

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
