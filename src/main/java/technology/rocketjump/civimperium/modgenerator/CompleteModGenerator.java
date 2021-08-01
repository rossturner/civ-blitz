package technology.rocketjump.civimperium.modgenerator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.sql.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static technology.rocketjump.civimperium.model.CardCategory.LeaderAbility;

@Component
public class CompleteModGenerator {

	private final ModHeaderGenerator modHeaderGenerator;
	private final ModInfoGenerator modInfoGenerator;
	private final CivilizationSqlGenerator civilizationSqlGenerator;
	private final CivTraitsSqlGenerator civTraitsSqlGenerator;
	private final ColorsSqlGenerator colorsSqlGenerator;
	private final ConfigurationSqlGenerator configurationSqlGenerator;
	private final GeographySqlGenerator geographySqlGenerator;
	private final IconsSqlGenerator iconsSqlGenerator;
	private final LeaderSqlGenerator leaderSqlGenerator;
	private final LeaderTextSqlGenerator leaderTextSqlGenerator;

	@Autowired
	public CompleteModGenerator(ModHeaderGenerator modHeaderGenerator, ModInfoGenerator modInfoGenerator,
								CivilizationSqlGenerator civilizationSqlGenerator, CivTraitsSqlGenerator civTraitsSqlGenerator,
								ColorsSqlGenerator colorsSqlGenerator, ConfigurationSqlGenerator configurationSqlGenerator,
								GeographySqlGenerator geographySqlGenerator, IconsSqlGenerator iconsSqlGenerator,
								LeaderSqlGenerator leaderSqlGenerator, LeaderTextSqlGenerator leaderTextSqlGenerator) {
		this.modHeaderGenerator = modHeaderGenerator;
		this.modInfoGenerator = modInfoGenerator;
		this.civilizationSqlGenerator = civilizationSqlGenerator;
		this.civTraitsSqlGenerator = civTraitsSqlGenerator;
		this.colorsSqlGenerator = colorsSqlGenerator;
		this.configurationSqlGenerator = configurationSqlGenerator;
		this.geographySqlGenerator = geographySqlGenerator;
		this.iconsSqlGenerator = iconsSqlGenerator;
		this.leaderSqlGenerator = leaderSqlGenerator;
		this.leaderTextSqlGenerator = leaderTextSqlGenerator;
	}

	public byte[] generateMod(Map<CardCategory, Card> selectedCards) throws IOException {
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
		String geographySqlContent = geographySqlGenerator.getGeographySql(header, selectedCards.get(CardCategory.CivilizationAbility).getCivilizationType());
		String iconsSqlContent = iconsSqlGenerator.getIconsSql(header, selectedCards);
		String leaderSqlContent = leaderSqlGenerator.getLeaderSql(header, selectedCards);
		String leaderTextSqlContent = leaderTextSqlGenerator.getLeaderTextSql(header, selectedCards);


		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		//simple file list, just for tests
		zipOutputStream.putNextEntry(new ZipEntry("Civilization.sql"));
		byte[] civSqlBytes = civilizationSqlContent.getBytes();
		zipOutputStream.write(civSqlBytes, 0, civSqlBytes.length);



		zipOutputStream.finish();
		zipOutputStream.flush();
		IOUtils.closeQuietly(zipOutputStream);
		IOUtils.closeQuietly(bufferedOutputStream);
		IOUtils.closeQuietly(byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

}
