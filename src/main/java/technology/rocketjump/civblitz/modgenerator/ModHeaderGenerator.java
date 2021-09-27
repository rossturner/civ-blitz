package technology.rocketjump.civblitz.modgenerator;

import org.h2.util.StringUtils;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;

import java.util.Map;
import java.util.UUID;

import static technology.rocketjump.civblitz.model.CardCategory.*;

@Component
public class ModHeaderGenerator {

	public ModHeader createFor(String matchName) {

		String description = "This mod consists of a selection of new civs to support the Civ Blitz match " + matchName;

		String modName = StringUtils.replaceAll(matchName, " ", "_");
		return new ModHeader(modName, description, UUID.nameUUIDFromBytes(modName.getBytes()));
	}

	public ModHeader createFor(Map<CardCategory, Card> selectedCards) {
		String name = buildName(selectedCards);

		StringBuilder descriptionBuilder = new StringBuilder();
		descriptionBuilder.append("This mod consists of a new civ using the ");
		descriptionBuilder.append(selectedCards.get(CivilizationAbility).getCivilizationFriendlyName()).append(" civ ability, ");
		descriptionBuilder.append(selectedCards.get(LeaderAbility).getBaseCardName()).append(" as the leader, the ");
		descriptionBuilder.append(selectedCards.get(UniqueInfrastructure).getBaseCardName()).append(" unique infrastructure (from ")
				.append(selectedCards.get(UniqueInfrastructure).getCivilizationFriendlyName()).append("), and the ");
		descriptionBuilder.append(selectedCards.get(UniqueUnit).getBaseCardName()).append(" unique unit (from ")
				.append(selectedCards.get(UniqueUnit).getCivilizationFriendlyName()).append(").");


		return new ModHeader(name, descriptionBuilder.toString(), UUID.nameUUIDFromBytes(name.getBytes()));
	}

	public static String buildName(Map<CardCategory, Card> selectedCards) {
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(getShortName(selectedCards.get(CivilizationAbility).getCivilizationFriendlyName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.LeaderAbility).getBaseCardName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.UniqueInfrastructure).getCivilizationFriendlyName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.UniqueUnit).getCivilizationFriendlyName()));
		return nameBuilder.toString();
	}

	private static String getShortName(String name) {
		StringBuilder shortNameBuilder = new StringBuilder();
		for (int cursor = 0; cursor < name.length(); cursor++) {
			char c = name.charAt(cursor);
			if (isLetter(c)) {
				shortNameBuilder.append(c);
				if (shortNameBuilder.length() >= 4) {
					break;
				}
			}
		}

		return shortNameBuilder.toString();
	}

	private static boolean isLetter(char c) {
		// only want A - Z, not unicode characters
		return (c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z');
	}
}
