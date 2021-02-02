
	package ar.edu.itba.paw.persistence.support;

	import java.util.Properties;

	import javax.persistence.EntityManagerFactory;
	import javax.sql.DataSource;

	import org.hsqldb.jdbc.JDBCDriver;
	import org.springframework.beans.factory.annotation.Qualifier;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.ComponentScan;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.Primary;
	import org.springframework.jdbc.datasource.SimpleDriverDataSource;
	import org.springframework.orm.jpa.JpaTransactionManager;
	import org.springframework.orm.jpa.JpaVendorAdapter;
	import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
	import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
	import org.springframework.transaction.PlatformTransactionManager;

	@ComponentScan({
		"ar.edu.itba.paw.persistence"
	})
	@Configuration
	public class PersistenceTestConfig {

		@Bean
		@Primary
		@Qualifier("realtime")
		public TestDataFactory factory() {

			return new TestDataFactory(dataSource());
		}

		@Bean
		@Primary
		@Qualifier("realtime")
		public DataSource dataSource() {

			final SimpleDriverDataSource dataSource
				= new SimpleDriverDataSource();

			dataSource.setDriverClass(JDBCDriver.class);
			dataSource.setUrl("jdbc:hsqldb:mem:paw-2017b-3");
			dataSource.setUsername("tester");
			dataSource.setPassword("tester:password");
			return dataSource;
		}

		/*********************************************************************/
		/* HIBERNATE/JPA */

		@Bean
		@Primary
		@Qualifier("realtime")
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

			final LocalContainerEntityManagerFactoryBean factoryBean
				= new LocalContainerEntityManagerFactoryBean();
			factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
			factoryBean.setDataSource(dataSource());

			final JpaVendorAdapter vendorAdapter
				= new HibernateJpaVendorAdapter();
			factoryBean.setJpaVendorAdapter(vendorAdapter);

			final Properties properties = new Properties();
			properties.setProperty("hibernate.hbm2ddl.auto", "update");
			properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
			properties.setProperty("hibernate.show_sql", "true");
			properties.setProperty("format_sql", "true");
			factoryBean.setJpaProperties(properties);

			return factoryBean;
		}

		@Bean
		@Primary
		@Qualifier("realtime")
		public PlatformTransactionManager transactionManager(
				final EntityManagerFactory factory) {

			return new JpaTransactionManager(factory);
		}
	}
