package ar.edu.itba.paw.model.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ContentType {

	IMAGE_PNG("image/png"),
	IMAGE_JPG("image/jpg"),
	IMAGE_JPEG("image/jpeg"),
	IMAGE_GIF("image/gif"),
	IMAGE_BMP("image/bmp"),
	APPLICATION_OCTET_STREAM("application/octet-stream");

	private static final Logger logger = LoggerFactory.getLogger(ContentType.class);
	private String contentType;

	private ContentType(final String contentType) {
		this.contentType = contentType;
	}

	@JsonValue
	public String getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return contentType;
	}

	@JsonCreator
	public static ContentType fromContentType(final String contentType) {
		logger.debug("fromContentType({})...", contentType);
		if (contentType == null) {
			return null;
		}
		for (final ContentType type : ContentType.values()) {
			if (type.toString().toLowerCase().equals(contentType.toLowerCase())) {
				logger.debug("...returning {}.", type);
				return type;
			}
		}
		logger.debug("...returning [null].");
		return null;
	}

}
