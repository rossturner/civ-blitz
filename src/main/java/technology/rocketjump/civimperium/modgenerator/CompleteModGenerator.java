package technology.rocketjump.civimperium.modgenerator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.infrastructurefix.InfrastructureFixFileProvider;
import technology.rocketjump.civimperium.infrastructurefix.StaticModFile;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.sql.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
	private final InfrastructureFixFileProvider infrastructureFixFileProvider;

	private List<ImperiumFileGenerator> fileGeneratorList = new ArrayList<>();

	@Autowired
	public CompleteModGenerator(ModHeaderGenerator modHeaderGenerator, ModInfoGenerator modInfoGenerator,
								CivilizationSqlGenerator civilizationSqlGenerator, CivTraitsSqlGenerator civTraitsSqlGenerator,
								ColorsSqlGenerator colorsSqlGenerator, ConfigurationSqlGenerator configurationSqlGenerator,
								GeographySqlGenerator geographySqlGenerator, IconsSqlGenerator iconsSqlGenerator,
								LeaderSqlGenerator leaderSqlGenerator, LeaderTextSqlGenerator leaderTextSqlGenerator, InfrastructureFixFileProvider infrastructureFixFileProvider) {
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
		this.infrastructureFixFileProvider = infrastructureFixFileProvider;

		fileGeneratorList.add(civilizationSqlGenerator);
		fileGeneratorList.add(civTraitsSqlGenerator);
		fileGeneratorList.add(colorsSqlGenerator);
		fileGeneratorList.add(configurationSqlGenerator);
		fileGeneratorList.add(geographySqlGenerator);
		fileGeneratorList.add(iconsSqlGenerator);
		fileGeneratorList.add(modInfoGenerator);
		fileGeneratorList.add(leaderSqlGenerator);
		fileGeneratorList.add(leaderTextSqlGenerator);
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

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		for (ImperiumFileGenerator generator : fileGeneratorList) {
			byte[] contentBytes = generator.getFileContents(header, selectedCards).getBytes();
			zipOutputStream.putNextEntry(new ZipEntry(generator.getFilename()));
			zipOutputStream.write(contentBytes, 0, contentBytes.length);
		}
		for (StaticModFile fixFile : infrastructureFixFileProvider.getAll()) {
			byte[] contentBytes = fixFile.getFileContent().getBytes();
			zipOutputStream.putNextEntry(new ZipEntry(fixFile.getFilename()));
			zipOutputStream.write(contentBytes, 0, contentBytes.length);
		}

		zipOutputStream.finish();
		zipOutputStream.flush();
		IOUtils.closeQuietly(zipOutputStream);
		IOUtils.closeQuietly(bufferedOutputStream);
		IOUtils.closeQuietly(byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

}
