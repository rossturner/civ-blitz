package technology.rocketjump.civblitz.matches.guilds;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class GuildDefinitionParser {

	private static final int MAX_IDENTIFIER_LENGTH = 28;
	private final String googleApiKey;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final GuildDefinitionRepo guildDefinitionRepo;

	private Map<ColumnHeader, Integer> columnIndices;

	@Autowired
	public GuildDefinitionParser(@Value("${google.api.key}") String googleApiKey,
								 GuildDefinitionRepo guildDefinitionRepo) {
		this.googleApiKey = googleApiKey;
		this.guildDefinitionRepo = guildDefinitionRepo;
	}

	public List<GuildDefinition> readFromGoogleSheet() throws JsonProcessingException {
		String requestUrl = "https://sheets.googleapis.com/v4/spreadsheets/1PNS3od_8Kh3LrH48WLQveBhVQ7CvDSwmOlwErP1Q8SM/" +
				"values/Guilds!A1:L300?key="+googleApiKey;

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

			List<GuildDefinition> definitions = new ArrayList<>();

			while (iterator.hasNext()) {
				JsonNode row = iterator.next();
				if (row.isEmpty()) {
					continue;
				}

				parse(row).ifPresent(definition -> {
					guildDefinitionRepo.add(definition);
					definitions.add(definition);
				});
			}

			return definitions;
		} else {
			throw new RuntimeException("Could not load objectives from google sheet, " + response.getBody());
		}
	}

	private Optional<GuildDefinition> parse(JsonNode row) {

		String identifier = getColumn(row, ColumnHeader.ID);
		String name = getColumn(row, ColumnHeader.Name);
		if (name.isEmpty()) {
			return Optional.empty();
		}
		String description = getColumn(row, ColumnHeader.Description);

		String category = getColumn(row, ColumnHeader.Category);
		String isActive = getColumn(row, ColumnHeader.Is_Active);

		if (identifier.isEmpty()) {
			identifier = toIdentifier(name);
		}

		GuildDefinition guildDefinition = new GuildDefinition(
				name, identifier, description, category, !isActive.equalsIgnoreCase("FALSE")
		);

		return Optional.of(guildDefinition);
	}

	private String toIdentifier(String name) {
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
		Integer columnIndex = columnIndices.get(columnHeader);
		if (columnIndex >= row.size()) {
			return Strings.EMPTY;
		} else {
			return row.get(columnIndex).asText();
		}
	}

	public enum ColumnHeader {

		ID,
		Name,
		Description,
		Ancient,
		Medieval,
		Industrial,
		Type,
		Category,
		Military,
		Is_Active

	}

}
