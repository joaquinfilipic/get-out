package ar.edu.itba.paw.model.support.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;

public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(final LocalDateTime timestamp) {
		if (timestamp != null) {
			return Timestamp.valueOf(timestamp);
		}
		else return null;
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Timestamp timestamp) {
		if (timestamp != null) {
			return timestamp.toLocalDateTime();
		}
		else return null;
	}
}
