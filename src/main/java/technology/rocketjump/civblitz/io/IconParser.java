package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.IconAtlasEntry;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class IconParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public IconParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String type = record.get("Type");
				String iconAtlas = record.get("IconAtlas");
				String index = record.get("Index");

				sourceDataRepo.addIconAtlasEntry(type, new IconAtlasEntry(iconAtlas, Integer.parseInt(index)));
			}
		}
	}

}
