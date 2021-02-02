package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.config.WebProperties;
import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.image.Image;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@ApplicationPath("/api")
@ComponentScan({
	"ar.edu.itba.paw.webapp.config",
	"ar.edu.itba.paw.webapp.rest",
	"ar.edu.itba.paw.service"
})
@Configuration
@EnableAspectJAutoProxy
@PropertySources({
	@PropertySource(value = "classpath:config/application.properties"),
	@PropertySource(value = "classpath:config/development.properties", ignoreResourceNotFound = true)
})
@Provider
public class WebConfig extends ResourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	private static final String DEFAULT_AVATAR_PATH = "/images/default-avatar.12110f2e7d.png";

	@Inject
	protected WebProperties config;

	public WebConfig() {
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
	}

	@Bean
	public static PropertyResourceConfigurer propertyResourceConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		final ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
		converter.setSupportedMediaTypes(getSupportedMediaTypes());
		return converter;
	}

	@Bean
	public Image defaultAvatar(final ServletContext context) {
		try (final InputStream stream = context.getResourceAsStream(DEFAULT_AVATAR_PATH)) {
			return new Image(IOUtils.toByteArray(stream), ContentType.IMAGE_PNG);
		}
		catch (final IOException exception) {
			logger.error("No se pudo obtener el avatar por defecto (ubicado en '{}').", DEFAULT_AVATAR_PATH);
			return new Image(new byte[1], ContentType.APPLICATION_OCTET_STREAM);
		}
	}

	protected List<MediaType> getSupportedMediaTypes() {
		return Arrays.asList(
				MediaType.APPLICATION_OCTET_STREAM,
				MediaType.IMAGE_PNG,
				MediaType.IMAGE_GIF,
				MediaType.IMAGE_JPEG);
	}
}
