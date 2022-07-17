package technology.rocketjump.civblitz.modgenerator;

import org.h2.util.StringUtils;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.model.Card;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.model.CardRarity;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static technology.rocketjump.civblitz.model.CardCategory.*;

@Component
public class ModHeaderGenerator {

	public ModHeader createFor(String matchName) {

		String description = "This mod consists of a selection of new civs to support the Civ Blitz match " + matchName;

		String modName = StringUtils.replaceAll(matchName, " ", "_");
		return new ModHeader(modName, description, UUID.nameUUIDFromBytes(modName.getBytes()));
	}

	public ModHeader createFor(List<Card> selectedCards) {
		String name = buildName(selectedCards);

		StringBuilder descriptionBuilder = new StringBuilder();
		descriptionBuilder.append("This mod consists of a new civ using the ");
		descriptionBuilder.append(find(selectedCards, CivilizationAbility).getCivilizationFriendlyName()).append(" civ ability, ");
		descriptionBuilder.append(find(selectedCards, LeaderAbility).getBaseCardName()).append(" as the leader, the ");
		descriptionBuilder.append(find(selectedCards, UniqueInfrastructure).getBaseCardName()).append(" unique infrastructure (from ")
				.append(find(selectedCards, UniqueInfrastructure).getCivilizationFriendlyName()).append("), and the ");
		descriptionBuilder.append(find(selectedCards, UniqueUnit).getBaseCardName()).append(" unique unit (from ")
				.append(find(selectedCards, UniqueUnit).getCivilizationFriendlyName()).append(").");


		return new ModHeader(name, descriptionBuilder.toString(), UUID.nameUUIDFromBytes(name.getBytes()));
	}

	public static String buildName(List<Card> selectedCards) {
		StringBuilder nameBuilder = new StringBuilder();
		selectedCards.sort(Comparator.comparing(Card::getCardCategory));
		for (Card card : selectedCards) {
			if (!card.getRarity().equals(CardRarity.Common)) {
				nameBuilder.append(card.getRarity().name().charAt(0));
			}
			if (card.getCardCategory().equals(LeaderAbility)) {
				nameBuilder.append(getShortName(card.getCivilizationFriendlyName()));
			} else {
				nameBuilder.append(getShortName(card.getBaseCardName()));
			}
		}
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

	private Card find(List<Card> selectedCards, CardCategory category) {
		return selectedCards.stream().filter(c -> c.getCardCategory().equals(category)).findAny().orElse(new Card());
	}
}
