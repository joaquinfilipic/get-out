package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.config.UploadProperties;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class UploadConfig {

	@Inject protected UploadProperties config;

	@Bean(name = "filterMultipartResolver")
	public MultipartResolver multipartResolver() {
		final CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxInMemorySize(config.getMaxInMemorySize());
		multipartResolver.setMaxUploadSize(config.getMaxSize());
		multipartResolver.setMaxUploadSizePerFile(config.getMaxSizePerFile());
		return multipartResolver;
	}
}
