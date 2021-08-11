package technology.rocketjump.civimperium;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class ResourceFiles {

	@Value("classpath:csv/LeaderTraits.csv")
	private Resource leaderTraitsFile;

	@Bean(name = "leaderTraits")
	public String leaderTraits() {
		try (InputStream is = leaderTraitsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivTraits.csv")
	private Resource civTraitsFile;

	@Bean(name = "civTraits")
	public String civTraits() {
		try (InputStream is = civTraitsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/subtypes.csv")
	private Resource subtypesFile;

	@Bean(name = "subtypes")
	public String subtypes() {
		try (InputStream is = subtypesFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivIcons.csv")
	private Resource civIconsFile;

	@Bean(name = "CivIcons")
	public String civIcons() {
		try (InputStream is = civIconsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/LeaderIcons.csv")
	private Resource leaderIconsFile;

	@Bean(name = "LeaderIcons")
	public String leaderIcons() {
		try (InputStream is = leaderIconsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/Players.csv")
	private Resource playersFile;

	@Bean(name = "Players")
	public String players() {
		try (InputStream is = playersFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/Civilizations.csv")
	private Resource civilizationsFile;

	@Bean(name = "Civilizations")
	public String civilizations() {
		try (InputStream is = civilizationsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:csv/CivilizationLeaders.csv")
	private Resource civilizationLeadersFile;

	@Bean(name = "CivilizationLeaders")
	public String civilizationLeaders() {
		try (InputStream is = civilizationLeadersFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:adjective_noun/adjectives.txt")
	private Resource adjectivesFile;

	@Bean(name = "adjectives")
	public String adjectives() {
		try (InputStream is = adjectivesFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("classpath:adjective_noun/nouns.txt")
	private Resource nounsFile;

	@Bean(name = "nouns")
	public String nouns() {
		try (InputStream is = nounsFile.getInputStream()) {
			return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
