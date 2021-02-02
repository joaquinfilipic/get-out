package ar.edu.itba.paw.model.support.converter;

import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.AttributeConverter;

public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {

	@Override
	public Time convertToDatabaseColumn(final LocalTime time) {
		if (time != null) {
			return Time.valueOf(time);
		}
		else return null;
	}

	@Override
	public LocalTime convertToEntityAttribute(final Time time) {
		if (time != null) {
			return time.toLocalTime();
		}
		else return null;
	}
}
