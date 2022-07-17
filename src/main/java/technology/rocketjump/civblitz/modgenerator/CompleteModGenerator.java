package technology.rocketjump.civblitz.modgenerator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civblitz.infrastructurefix.InfrastructureFixFileProvider;
import technology.rocketjump.civblitz.infrastructurefix.StaticModFile;
import technology.rocketjump.civblitz.model.CardCategory;
import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;
import technology.rocketjump.civblitz.modgenerator.sql.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	private List<BlitzFileGenerator> fileGeneratorList = new ArrayList<>();

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

	public byte[] generateMod(String matchName, List<ModdedCivInfo> moddedCivs) throws IOException {
		for (ModdedCivInfo civInfo : moddedCivs) {
			if (civInfo.selectedCards.size() != 4) {
				throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed a map of 4 cards");
			}
			for (CardCategory cardCategory : CardCategory.mainCategories) {
				if (civInfo.selectedCards.stream().noneMatch(c -> c.getCardCategory().equals(cardCategory))) {
					throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed one card in each category");
				}
			}
		}

		ModHeader header = modHeaderGenerator.createFor(matchName);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		for (BlitzFileGenerator generator : fileGeneratorList) {
			byte[] contentBytes = generator.getFileContents(header, moddedCivs).getBytes();
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

	public byte[] generateMod(ModdedCivInfo civInfo) throws IOException {
		if (civInfo.selectedCards.size() < 4) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed a map of at least 4 cards");
		}
		for (CardCategory cardCategory : CardCategory.mainCategories) {
			if (civInfo.selectedCards.stream().noneMatch(c -> c.getCardCategory().equals(cardCategory))) {
				throw new IllegalArgumentException(getClass().getSimpleName() + " must be passed one card in each category");
			}
		}

		ModHeader header = modHeaderGenerator.createFor(civInfo.selectedCards);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

		for (BlitzFileGenerator generator : fileGeneratorList) {
			byte[] contentBytes = generator.getFileContents(header, civInfo).getBytes();
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
