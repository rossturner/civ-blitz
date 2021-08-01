package technology.rocketjump.civimperium.modgenerator;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;
import java.util.UUID;

import static technology.rocketjump.civimperium.model.CardCategory.*;

@Component
public class ModHeaderGenerator {

	public ModHeader createFor(Map<CardCategory, Card> selectedCards) {
		StringBuilder nameBuilder = new StringBuilder();

		nameBuilder.append(getShortName(selectedCards.get(CivilizationAbility).getCivilizationFriendlyName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.LeaderAbility).getCardName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.UniqueInfrastructure).getCivilizationFriendlyName()));
		nameBuilder.append(getShortName(selectedCards.get(CardCategory.UniqueUnit).getCivilizationFriendlyName()));
		String name = nameBuilder.toString();

		StringBuilder descriptionBuilder = new StringBuilder();
		descriptionBuilder.append("This mod consists of a new civ using the ");
		descriptionBuilder.append(selectedCards.get(CivilizationAbility).getCivilizationFriendlyName()).append(" civ ability, ");
		descriptionBuilder.append(selectedCards.get(LeaderAbility).getCardName()).append(" as the leader, the ");
		descriptionBuilder.append(selectedCards.get(UniqueInfrastructure).getCardName()).append(" unique infrastructure (from ")
				.append(selectedCards.get(UniqueInfrastructure).getCivilizationFriendlyName()).append("), and the ");
		descriptionBuilder.append(selectedCards.get(UniqueUnit).getCardName()).append(" unique unit (from ")
				.append(selectedCards.get(UniqueUnit).getCivilizationFriendlyName()).append(").");


		return new ModHeader(name, descriptionBuilder.toString(), UUID.nameUUIDFromBytes(name.getBytes()));
	}

	private String getShortName(String name) {
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
