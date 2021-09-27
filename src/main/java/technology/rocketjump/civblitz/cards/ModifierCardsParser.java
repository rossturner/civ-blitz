package technology.rocketjump.civblitz.cards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import technology.rocketjump.civblitz.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static technology.rocketjump.civblitz.matches.objectives.ObjectiveDefinitionParser.toIdentifier;
import static technology.rocketjump.civblitz.model.CardRarity.Common;

@Component
public class ModifierCardsParser {

	private Logger logger = LoggerFactory.getLogger(ModifierCardsParser.class);

	private final String googleApiKey;
	private final SourceDataRepo sourceDataRepo;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	private Map<ColumnHeader, Integer> columnIndices;

	@Autowired
	public ModifierCardsParser(@Value("${google.api.key}") String googleApiKey,
							   SourceDataRepo sourceDataRepo) {
		this.googleApiKey = googleApiKey;
		this.sourceDataRepo = sourceDataRepo;
	}

	public void readFromGoogleSheet() throws JsonProcessingException {
		String requestUrl = "https://sheets.googleapis.com/v4/spreadsheets/1PNS3od_8Kh3LrH48WLQveBhVQ7CvDSwmOlwErP1Q8SM/" +
				"values/Modifiers!A1:L300?key="+googleApiKey;

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

			while (iterator.hasNext()) {
				JsonNode row = iterator.next();
				if (row.isEmpty()) {
					continue;
				}

				parse(row).ifPresent(card -> {
					sourceDataRepo.add(card);
				});
			}
		} else {
			throw new RuntimeException("Could not load modified cards from google sheet, " + response.getBody());
		}
	}

	private Optional<Card> parse(JsonNode row) {

		String identifier = getColumn(row, ColumnHeader.ID);
		String name = getColumn(row, ColumnHeader.Name);
		String type = getColumn(row, ColumnHeader.Type);
		String active = getColumn(row, ColumnHeader.Is_Active);
		if (name.isEmpty() || type.isEmpty() || "FALSE".equalsIgnoreCase(active)) {
			return Optional.empty();
		}
		if (identifier.isEmpty()) {
			identifier = toIdentifier(name);
		}

		if (type.equalsIgnoreCase("Power")) {
			return parsePowerCard(row, identifier, name);
		} else if (type.equalsIgnoreCase("Upgrade")) {
			return parseUpgradeCard(row, identifier, name);
		} else {
			logger.error("Unrecognised Type field in Modifiers sheet: " + type);
			return Optional.empty();
		}
	}

	private Optional<Card> parsePowerCard(JsonNode row, String identifier, String name) {
		String belongsTo = getColumn(row, ColumnHeader.BelongsTo);
		Card civAbilityCard = sourceDataRepo.civAbilityCardByFriendlyName.get(belongsTo);
		if (civAbilityCard == null) {
			logger.error("Can not find civ ability by friendly name " + belongsTo);
			return Optional.empty();
		}

		String addsTraitType = getColumn(row, ColumnHeader.AddsTraitType);
		String addsModifierIds = getColumn(row, ColumnHeader.AddsModifierIds);
		List<String> modifierIds = List.of(addsModifierIds.split("\\|"));

		if (addsTraitType.isEmpty() && modifierIds.isEmpty()) {
			logger.error("No trait type or modifier IDs granted by power " + name);
			return Optional.empty();
		}

		CardRarity rarity = EnumUtils.getEnum(CardRarity.class, getColumn(row, ColumnHeader.Rarity));
		if (rarity == null) {
			logger.error("Did not recognise rarity: " + getColumn(row, ColumnHeader.Rarity) +" for power " + name);
			return Optional.empty();
		}

		Card powerCard = new Card();
		powerCard.setIdentifier(identifier);
		powerCard.setCardCategory(CardCategory.Power);
		powerCard.setSuperCategory(SuperCategory.Power);
		powerCard.setBaseCardName(civAbilityCard.getBaseCardName());
		powerCard.setEnhancedCardName(name);
		powerCard.setEnhancedCardDescription(getColumn(row, ColumnHeader.Description));
		powerCard.setRarity(rarity);
		powerCard.setCivilizationType(civAbilityCard.getCivilizationType());
		powerCard.setCivilizationFriendlyName(civAbilityCard.getCivilizationFriendlyName());
		powerCard.setMediaName(civAbilityCard.getMediaName());
		powerCard.setRequiredDlc(civAbilityCard.getRequiredDlc());
		if (!addsTraitType.isEmpty()) {
			powerCard.setGrantsTraitType(Optional.of(addsTraitType));
		}
		powerCard.getModifierIds().addAll(modifierIds.stream().filter(m -> m.length() > 0).collect(Collectors.toList()));

		return Optional.of(powerCard);
	}

	private Optional<Card> parseUpgradeCard(JsonNode row, String identifier, String name) {

		String upgradesCard = getColumn(row, ColumnHeader.UpgradesCard);
		Card baseCard = sourceDataRepo.getByIdentifier(upgradesCard);
		if (baseCard == null) {
			logger.error("Can not find base card " + upgradesCard + " for upgrade " + name);
			return Optional.empty();
		}


		CardRarity rarity = EnumUtils.getEnum(CardRarity.class, getColumn(row, ColumnHeader.Rarity));
		if (rarity == null) {
			logger.error("Did not recognise rarity: " + getColumn(row, ColumnHeader.Rarity) +" for power " + name);
			return Optional.empty();
		} else if (rarity.equals(Common)) {
			logger.error("Upgrade cards must be higher rarity than common");
			return Optional.empty();
		}

		identifier = rarity.name().toUpperCase()+"_"+upgradesCard;
		if (sourceDataRepo.getByIdentifier(identifier) != null) {
			logger.error("Duplicate card with identifier " + identifier);
			return Optional.empty();
		}

		String gameplaySql = getColumn(row, ColumnHeader.SQL);
		String localisationSql = getColumn(row, ColumnHeader.LocalisationSQL);

		Card upgradedCard = new Card();
		upgradedCard.setIdentifier(identifier);
		upgradedCard.setBaseCardName(baseCard.getBaseCardName());
		upgradedCard.setBaseCardDescription(baseCard.getBaseCardDescription());
		upgradedCard.setEnhancedCardName(name);
		upgradedCard.setEnhancedCardDescription(getColumn(row, ColumnHeader.Description));
		upgradedCard.setTraitType(baseCard.getTraitType());
		upgradedCard.setCivilizationType(baseCard.getCivilizationType());
		upgradedCard.setCardCategory(baseCard.getCardCategory());
		upgradedCard.setSuperCategory(SuperCategory.Upgrade);
		upgradedCard.setRarity(rarity);
		upgradedCard.setCivilizationFriendlyName(baseCard.getCivilizationFriendlyName());
		upgradedCard.setSubtype(baseCard.getSubtype());
		upgradedCard.setMediaName(baseCard.getMediaName());
		upgradedCard.setRequiredDlc(baseCard.getRequiredDlc());
		upgradedCard.getModifierIds().addAll(baseCard.getModifierIds());
		upgradedCard.setGameplaySQL(gameplaySql);
		upgradedCard.setLocalisationSQL(localisationSql);

		return Optional.of(upgradedCard);
	}


	private String getColumn(JsonNode row, ColumnHeader columnHeader) {
		JsonNode node = row.get(columnIndices.get(columnHeader));
		if (node == null) {
			return Strings.EMPTY;
		} else {
			return node.asText();
		}
	}

	public enum ColumnHeader {

		ID,
		Name,
		Rarity,
		Description,
		Type,
		BelongsTo,
		AddsTraitType,
		AddsModifierIds,
		Is_Active,
		SQL,
		UpgradesCard,
		LocalisationSQL

	}

}
