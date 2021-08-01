package technology.rocketjump.civimperium.modgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.sql.CivTraitsSqlGenerator;
import technology.rocketjump.civimperium.modgenerator.sql.CivilizationSqlGenerator;
import technology.rocketjump.civimperium.modgenerator.sql.ColorsSqlGenerator;
import technology.rocketjump.civimperium.modgenerator.sql.ConfigurationSqlGenerator;

import java.util.Map;

import static technology.rocketjump.civimperium.model.CardCategory.LeaderAbility;

@Component
public class CompleteModGenerator {

	private final ModHeaderGenerator modHeaderGenerator;
	private final ModInfoGenerator modInfoGenerator;
	private final CivilizationSqlGenerator civilizationSqlGenerator;
	private final CivTraitsSqlGenerator civTraitsSqlGenerator;
	private final ColorsSqlGenerator colorsSqlGenerator;
	private final ConfigurationSqlGenerator configurationSqlGenerator;

	@Autowired
	public CompleteModGenerator(ModHeaderGenerator modHeaderGenerator, ModInfoGenerator modInfoGenerator,
								CivilizationSqlGenerator civilizationSqlGenerator, CivTraitsSqlGenerator civTraitsSqlGenerator,
								ColorsSqlGenerator colorsSqlGenerator, ConfigurationSqlGenerator configurationSqlGenerator) {
		this.modHeaderGenerator = modHeaderGenerator;
		this.modInfoGenerator = modInfoGenerator;
		this.civilizationSqlGenerator = civilizationSqlGenerator;
		this.civTraitsSqlGenerator = civTraitsSqlGenerator;
		this.colorsSqlGenerator = colorsSqlGenerator;
		this.configurationSqlGenerator = configurationSqlGenerator;
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
		String civilizationSqlContent = civilizationSqlGenerator.getCivilizationSql(header,
				selectedCards.get(CardCategory.CivilizationAbility), selectedCards.get(LeaderAbility),
				selectedCards.get(CardCategory.CivilizationAbility).getCivilizationType());
		String civTraitsSqlContent = civTraitsSqlGenerator.getCivTraits(header, selectedCards);
		String colorsSqlContent = colorsSqlGenerator.getColorsSql(header, selectedCards.get(LeaderAbility).getLeaderType().get());
		String configurationSqlContent = configurationSqlGenerator.getConfigurationSql(header, selectedCards);

		return configurationSqlContent;
	}

}
