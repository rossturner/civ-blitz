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

}
