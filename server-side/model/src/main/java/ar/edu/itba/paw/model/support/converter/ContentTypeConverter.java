package ar.edu.itba.paw.model.support.converter;

import ar.edu.itba.paw.model.image.ContentType;
import javax.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentTypeConverter implements AttributeConverter<ContentType, String> {

	private static final Logger logger = LoggerFactory.getLogger(ContentTypeConverter.class);

	@Override
	public String convertToDatabaseColumn(final ContentType contentType) {
		logger.debug("convertToDatabaseColumn({})...", contentType);
		if (contentType != null) {
			logger.debug("...returning {}.", contentType.toString());
			return contentType.toString();
		}
		logger.debug("...returning [null].");
		return null;
	}

	@Override
	public ContentType convertToEntityAttribute(final String contentType) {
		logger.debug("convertToEntityAttribute({})...", contentType);
		if (contentType != null) {
			return ContentType.fromContentType(contentType);
		}
		logger.debug("...returning [null].");
		return null;
	}

}
