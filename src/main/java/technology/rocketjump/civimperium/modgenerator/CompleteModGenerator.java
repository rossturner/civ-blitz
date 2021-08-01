package technology.rocketjump.civimperium.modgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;

@Component
public class CompleteModGenerator {

	private final ModHeaderGenerator modHeaderGenerator;
	private final ModInfoGenerator modInfoGenerator;

	@Autowired
	public CompleteModGenerator(ModHeaderGenerator modHeaderGenerator, ModInfoGenerator modInfoGenerator) {
		this.modHeaderGenerator = modHeaderGenerator;
		this.modInfoGenerator = modInfoGenerator;
	}

	public String generateMod(Map<CardCategory, Card> selectedCards) {
		if (selectedCards.size() != 4) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed a map of 4 cards");
		}
		for (CardCategory cardCategory : CardCategory.values()) {
			if (!selectedCards.containsKey(cardCategory)) {
				throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed one card in each category");
			}
		}

		ModHeader header = modHeaderGenerator.createFor(selectedCards);

		String modInfoContent = modInfoGenerator.getModInfoContent(header);

		return modInfoContent;
	}

}
