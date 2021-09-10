package technology.rocketjump.civblitz;

import org.jooq.impl.DataSourceConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

	private DataSource dataSource;

	@Autowired
	public PersistenceConfiguration(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public DataSourceConnectionProvider connectionProvider() {
		return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
	}

}
