package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.config.PersistenceProperties;
import ar.edu.itba.paw.interfaces.config.SecurityProperties;
import ar.edu.itba.paw.interfaces.config.UploadProperties;
import ar.edu.itba.paw.interfaces.config.WebProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GetOutConfig
	implements PersistenceProperties, SecurityProperties, UploadProperties, WebProperties {

	private static final Logger logger = LoggerFactory.getLogger(GetOutConfig.class);

	// Persistence properties...
	@Value("classpath:schema.sql")
	protected Resource schema;

	@Value("classpath:data.sql")
	protected Resource data;

	@Value("${jdbc.dbms}")
	protected String dbms;

	@Value("${jdbc.host}")
	protected String host;

	@Value("${jdbc.port}")
	protected String port;

	@Value("${jdbc.database}")
	protected String database;

	@Value("${jdbc.username}")
	protected String username;

	@Value("${jdbc.password}")
	protected String password;

	@Value("${deploy.default.data}")
	protected boolean deployData;

	@Value("${hibernate.hbm2ddl}")
	protected String hbm2ddl;

	@Value("${hibernate.dialect}")
	protected String dialect;

	@Value("${hibernate.sql.show}")
	protected boolean showSQL;

	@Value("${hibernate.sql.format}")
	protected boolean formatSQL;

	// Security properties...
	@Value("classpath:private/remember.key")
	protected Resource rememberKey;

	@Value("classpath:private/jwt.key")
	protected Resource jwtSecret;

	@Value("${bcrypt.strength.rounds}")
	protected int strengthRounds;

	@Value("${token.validity.seconds}")
	protected int tokenValidity;

	@Value("${jwt.expiration.time.milliseconds}")
	protected long jwtExpiration;

	// Upload properties...
	@Value("${upload.max.memory}")
	protected int maxInMemorySize;

	@Value("${upload.max.request.size}")
	protected long maxUploadSize;

	@Value("${upload.max.file.size}")
	protected long maxUploadSizePerFile;

	// Web properties...
	@Value("${i18n.cache.seconds}")
	protected int cacheSeconds;

	@Override
	public Resource [] getScripts() {
		if (deployData) {
			return new Resource [] {
				schema, data
			};
		}
		else {
			return new Resource [] {
				schema
			};
		}
	}

	@Override
	public String getDBMS() {
		return dbms;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getPort() {
		return port;
	}

	@Override
	public String getDatabase() {
		return database;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getHibernateAuto() {
		return hbm2ddl;
	}

	@Override
	public String getDialect() {
		return dialect;
	}

	@Override
	public boolean showSQL() {
		return showSQL;
	}

	@Override
	public boolean formatSQL() {
		return formatSQL;
	}

	@Override @Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(strengthRounds);
	}

	@Override
	public String getRememberKey() {
		return readFromResource(rememberKey);
	}

	@Override
	public int getTokenValidityInSeconds() {
		return tokenValidity;
	}

	@Override
	public byte [] getJWTSecret() {
		return Base64.decodeBase64(readFromResource(jwtSecret));
	}

	@Override
	public long getJWTExpirationTimeInMilliseconds() {
		return jwtExpiration;
	}

	@Override
	public int getMaxInMemorySize() {
		return maxInMemorySize;
	}

	@Override
	public long getMaxSize() {
		return maxUploadSize;
	}

	@Override
	public long getMaxSizePerFile() {
		return maxUploadSizePerFile;
	}

	@Override
	public int getCacheInSeconds() {
		return cacheSeconds;
	}

	// Mandar a una clase de soporte.
	protected String readFromResource(final Resource resource) {
		try (
			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final InputStream stream = resource.getInputStream();
		) {
			final byte [] buffer = new byte [4096];
			int length;
			while ((length = stream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			return result.toString(StandardCharsets.UTF_8.name());
		}
		catch (final IOException exception) {
			logger.error("No se pudo obtener la clave del recurso '{}'.", resource.getFilename());
			return "WE-ARE-FUCKED" + hashCode();
		}
	}
}
