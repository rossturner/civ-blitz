package technology.rocketjump.civimperium.modgenerator.sql;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;

@Component
public class CivTraitsSqlGenerator {

	public String getCivTraits(ModHeader modHeader, Map<CardCategory, Card> selectedCards) {
		String modName = modHeader.modName.toUpperCase();
		StringBuilder sqlBuilder = new StringBuilder();

		for (CardCategory cardCategory : CardCategory.values()) {
			if (!cardCategory.equals(CardCategory.LeaderAbility)) {
				Card card = selectedCards.get(cardCategory);
				addLine(sqlBuilder, card.getTraitType(), modName);
				if (card.getGrantsTraitType().isPresent()) {
					addLine(sqlBuilder, card.getGrantsTraitType().get(), modName);
				}
			}
		}

		return sqlBuilder.toString();
	}

	private void addLine(StringBuilder sqlBuilder, String traitType, String modName) {
		sqlBuilder.append("INSERT OR REPLACE INTO CivilizationTraits (TraitType, CivilizationType) VALUES ('")
				.append(traitType).append("', 'CIVILIZATION_IMP_").append(modName).append("');\n");
	}

}
