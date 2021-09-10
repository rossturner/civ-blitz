package technology.rocketjump.civblitz.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.SourceDataRepo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class PlayersParser {

	private final SourceDataRepo sourceDataRepo;

	@Autowired
	public PlayersParser(SourceDataRepo sourceDataRepo) {
		this.sourceDataRepo = sourceDataRepo;
	}

	public void parse(String resourceContent) throws IOException {
		try (Reader input = new StringReader(resourceContent)) {
			CSVParser parsed = CSVFormat.DEFAULT
					.withFirstRecordAsHeader()
					.parse(input);

			for (CSVRecord record : parsed.getRecords()) {
				String civType = record.get("CivilizationType");
				String leaderType = record.get("LeaderType");
				String portrait = record.get("Portrait");
				String portraitBackground = record.get("PortraitBackground");

				sourceDataRepo.leaderNameByLeaderType.put(leaderType, record.get("LeaderName"));
				sourceDataRepo.leaderIconByLeaderType.put(leaderType, record.get("LeaderIcon"));
				sourceDataRepo.leaderAbilityIconByLeaderType.put(leaderType, record.get("LeaderAbilityIcon"));
				sourceDataRepo.civNameByCivType.put(civType, record.get("CivilizationName"));
				sourceDataRepo.civIconByCivType.put(civType, record.get("CivilizationAbilityIcon"));
				sourceDataRepo.civAbilityNameByCivType.put(civType, record.get("CivilizationAbilityName"));
				sourceDataRepo.civAbilityDescByCivType.put(civType, record.get("CivilizationAbilityDescription"));
				sourceDataRepo.civAbilityIconByCivType.put(civType, record.get("CivilizationAbilityIcon"));
				sourceDataRepo.portraitsByLeaderType.put(leaderType, portrait);
				sourceDataRepo.portraitBackgroundsByLeaderType.put(leaderType, portraitBackground);
			}
		}
	}

}
