package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.config.PersistenceProperties;
import java.util.Properties;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan("ar.edu.itba.paw.persistence")
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

	@Inject protected PersistenceProperties config;

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
		final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
		factoryBean.setDataSource(dataSource());
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		final Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", config.getHibernateAuto());
		properties.setProperty("hibernate.dialect", config.getDialect());
		properties.setProperty("hibernate.show_sql", String.valueOf(config.showSQL()));
		properties.setProperty("format_sql", String.valueOf(config.formatSQL()));
		factoryBean.setJpaProperties(properties);
		return factoryBean;
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager(final EntityManagerFactory factory) {
		return new JpaTransactionManager(factory);
	}

	@Bean
	public DataSource dataSource() {
		final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(org.postgresql.Driver.class);
		dataSource.setUrl(config.getDatabaseURL());
		dataSource.setUsername(config.getUsername());
		dataSource.setPassword(config.getPassword());
		return dataSource;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource source) {
		final DataSourceInitializer dsi = new DataSourceInitializer();
		dsi.setDataSource(source);
		dsi.setDatabasePopulator(databasePopulator());
		return dsi;
	}

	protected DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
		dbp.addScripts(config.getScripts());
		return dbp;
	}
}
