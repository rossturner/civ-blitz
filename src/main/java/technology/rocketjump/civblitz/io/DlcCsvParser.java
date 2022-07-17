package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

@Component
public class DlcCsvParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public DlcCsvParser(SourceDataRepo sourceDataRepe) {
		this.sourceDataRepo = sourceDataRepe;
	}

	public void parse(String civDlcContent, String leaderDlcContent) throws IOException {
		try (Reader input = new StringReader(civDlcContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civType = record.get("CivilizationType").trim();
				String dlcName = record.get("DLC").trim();

				for (CardCategory category : List.of(CardCategory.CivilizationAbility, CardCategory.UniqueInfrastructure, CardCategory.UniqueUnit)) {
					sourceDataRepo.getByCategory(category, Optional.empty())
							.stream().filter(card -> card.getCivilizationType().equals(civType))
							.forEach(card -> {
								card.setRequiredDlc(dlcName);
							});
				}
			}
		}

		try (Reader input = new StringReader(leaderDlcContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String leaderType = record.get("LeaderType").trim();
				String dlcName = record.get("DLC").trim();

				sourceDataRepo.getByCategory(CardCategory.LeaderAbility, Optional.empty())
						.stream().filter(card -> card.getLeaderType().orElse("").equals(leaderType))
						.forEach(card -> {
							card.setRequiredDlc(dlcName);
						});
			}
		}
	}
}
