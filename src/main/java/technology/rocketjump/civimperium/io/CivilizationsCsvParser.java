package technology.rocketjump.civimperium.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class CivilizationsCsvParser {

	private final SourceDataRepo sourceDataRepe;

	@Autowired
	public CivilizationsCsvParser(SourceDataRepo sourceDataRepe) {
		this.sourceDataRepe = sourceDataRepe;
	}

	public void parse(String ciCsvContent) throws IOException {
		try (Reader input = new StringReader(ciCsvContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civType = record.get("CivilizationType");

				sourceDataRepe.civilizationCsvRecordsByCivType.put(civType, record);
			}
		}
	}
}
