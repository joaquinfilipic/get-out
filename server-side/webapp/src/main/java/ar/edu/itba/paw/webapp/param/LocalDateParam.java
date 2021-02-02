package ar.edu.itba.paw.webapp.param;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;

public class LocalDateParam {
	
	private final LocalDate date;
	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
	
	/**
	 * This method enables the usage of the class as a QueryParam parameter.
	 */
	public static LocalDateParam valueOf(String dateString) {
		return new LocalDateParam(dateString);
	}
	
	public LocalDateParam(String dateString) throws GetOutException {
		try {			
			this.date = LocalDate.parse(dateString, formatter);
		} catch (DateTimeParseException exception) {
			throw GetOutException.of(ErrorType.INVALID_PATH_PARAMETER);
		}
	}
	
	public LocalDate getDate() {
		return date;
	}

}
