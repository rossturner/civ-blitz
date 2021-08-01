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
public class PortraitsParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public PortraitsParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String leaderType = record.get("LeaderType");
				String portrait = record.get("Portrait");
				String portraitBackground = record.get("PortraitBackground");

				sourceDataRepo.portraitsByLeaderType.put(leaderType, portrait);
				sourceDataRepo.portraitBackgroundsByLeaderType.put(leaderType, portraitBackground);
			}
		}
	}

}
