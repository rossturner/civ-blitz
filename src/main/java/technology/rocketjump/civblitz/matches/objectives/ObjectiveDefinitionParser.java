package technology.rocketjump.civblitz.matches.objectives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import technology.rocketjump.civblitz.mapgen.StartEra;

import java.util.*;

@Component
public class ObjectiveDefinitionParser {

	public static final int MAX_IDENTIFIER_LENGTH = 28;
	private final String googleApiKey;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ObjectiveDefinitionRepo objectiveDefinitionRepo;

	private Map<ColumnHeader, Integer> columnIndices;

	@Autowired
	public ObjectiveDefinitionParser(@Value("${google.api.key}") String googleApiKey,
									 ObjectiveDefinitionRepo objectiveDefinitionRepo) {
		this.googleApiKey = googleApiKey;
		this.objectiveDefinitionRepo = objectiveDefinitionRepo;
	}

	public List<ObjectiveDefinition> readFromGoogleSheet() throws JsonProcessingException {
		String requestUrl = "https://sheets.googleapis.com/v4/spreadsheets/1PNS3od_8Kh3LrH48WLQveBhVQ7CvDSwmOlwErP1Q8SM/" +
				"values/A1:L300?key="+googleApiKey;

		ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			JsonNode root = objectMapper.readTree(response.getBody());

			Iterator<JsonNode> iterator = root.get("values").elements();

			JsonNode headers = iterator.next();
			columnIndices = new LinkedHashMap<>();
			for (int cursor = 0; cursor < headers.size(); cursor++) {
				ColumnHeader columnHeader = EnumUtils.getEnum(ColumnHeader.class, headers.get(cursor).asText());
				if (columnHeader != null) {
					columnIndices.put(columnHeader, cursor);
				}
			}

			List<ObjectiveDefinition> definitions = new ArrayList<>();

			while (iterator.hasNext()) {
				JsonNode row = iterator.next();
				if (row.isEmpty()) {
					continue;
				}

				parse(row).ifPresent(definition -> {
					objectiveDefinitionRepo.add(definition);
					definitions.add(definition);
				});
			}

			return definitions;
		} else {
			throw new RuntimeException("Could not load objectives from google sheet, " + response.getBody());
		}
	}

	private Optional<ObjectiveDefinition> parse(JsonNode row) {

		String identifier = getColumn(row, ColumnHeader.ID);
		String name = getColumn(row, ColumnHeader.Name);
		if (name.isEmpty()) {
			return Optional.empty();
		}
		String description = getColumn(row, ColumnHeader.Description);

		String ancientStars = getColumn(row, ColumnHeader.Ancient);
		String medievalStars = getColumn(row, ColumnHeader.Medieval);
		String industrialStars = getColumn(row, ColumnHeader.Industrial);

		String type = getColumn(row, ColumnHeader.Type);
		String military = getColumn(row, ColumnHeader.Military);
		String isActive = getColumn(row, ColumnHeader.Is_Active);

		if (identifier.isEmpty()) {
			identifier = toIdentifier(name);
		}

		ObjectiveDefinition.ObjectiveType objectiveType = EnumUtils.getEnum(ObjectiveDefinition.ObjectiveType.class, type);
		if (objectiveType == null) {
			System.err.println("Can not parse " + type + " as ObjectiveType for " + name);
			return Optional.empty();
		}

		ObjectiveDefinition objectiveDefinition = new ObjectiveDefinition(
				name, identifier, description, objectiveType, military.equalsIgnoreCase("TRUE"), isActive.equalsIgnoreCase("TRUE")
		);

		if (!ancientStars.isEmpty()) {
			try {
				Integer ancientStarsNum = Integer.parseInt(ancientStars);
				objectiveDefinition.setStars(StartEra.Ancient, ancientStarsNum);
			} catch (NumberFormatException e) {
				System.err.println("Can not parse " + ancientStars + " to int from objective " + name);
				return Optional.empty();
			}
		}

		if (!medievalStars.isEmpty()) {
			try {
				Integer starsNum = Integer.parseInt(medievalStars);
				objectiveDefinition.setStars(StartEra.Medieval, starsNum);
			} catch (NumberFormatException e) {
				System.err.println("Can not parse " + medievalStars + " to int from objective " + name);
				return Optional.empty();
			}
		}

		if (!industrialStars.isEmpty()) {
			try {
				Integer starsNum = Integer.parseInt(industrialStars);
				objectiveDefinition.setStars(StartEra.Industrial, starsNum);
			} catch (NumberFormatException e) {
				System.err.println("Can not parse " + industrialStars + " to int from objective " + name);
				return Optional.empty();
			}
		}

		return Optional.of(objectiveDefinition);
	}

	public static String toIdentifier(String name) {
		StringBuilder identifier = new StringBuilder();

		for (int cursor = 0; cursor < name.length() && cursor < MAX_IDENTIFIER_LENGTH; cursor++) {
			char character = name.charAt(cursor);
			if (character == ' ') {
				identifier.append("_");
			} else if (CharUtils.isAsciiAlpha(character)) {
				identifier.append(CharUtils.toString(character).toUpperCase());
			}
		}

		return identifier.toString();
	}

	private String getColumn(JsonNode row, ColumnHeader columnHeader) {
		return row.get(columnIndices.get(columnHeader)).asText();
	}

	public enum ColumnHeader {

		ID,
		Name,
		Description,
		Ancient,
		Medieval,
		Industrial,
		Type,
		Military,
		Is_Active

	}

}
