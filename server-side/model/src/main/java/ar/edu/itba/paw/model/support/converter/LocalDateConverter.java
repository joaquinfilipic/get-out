package ar.edu.itba.paw.model.support.converter;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;

public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(final LocalDate date) {
		if (date != null) {
			return Date.valueOf(date);
		}
		else return null;
	}

	@Override
	public LocalDate convertToEntityAttribute(final Date date) {
		if (date != null) {
			return date.toLocalDate();
		}
		else return null;
	}
}
