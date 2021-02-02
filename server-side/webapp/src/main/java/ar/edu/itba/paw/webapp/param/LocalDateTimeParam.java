package ar.edu.itba.paw.webapp.param;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;

public class LocalDateTimeParam {
	
	private final LocalDateTime date;
	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	/**
	 * This method enables the usage of the class as a QueryParam parameter.
	 */
	public static LocalDateTimeParam valueOf(String dateString) {
		return new LocalDateTimeParam(dateString);
	}
	
	public LocalDateTimeParam(String dateString) throws GetOutException {
		try {
			this.date = LocalDateTime.parse(dateString, formatter);
		} catch (DateTimeParseException exception) {
			throw GetOutException.of(ErrorType.INVALID_PATH_PARAMETER);
		}
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public static Optional<LocalDateTime> getOptionalOf(LocalDateTimeParam param) {
		LocalDateTime date = null;
		if (param != null) {
			date = param.getDate();
		}
		return Optional.ofNullable(date);
	}
	
}
