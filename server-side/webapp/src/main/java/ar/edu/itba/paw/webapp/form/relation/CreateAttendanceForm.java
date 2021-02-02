package ar.edu.itba.paw.webapp.form.relation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class CreateAttendanceForm {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
	
	@NotNull
	@Digits(integer = 10, fraction = 0)
	@Exists(source = PubService.class)
	private Long pubId;
	
	@NotBlank
	@Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")
	private String date;
	
	public Long getPubId() {
		return pubId;
	}
	
	public LocalDate getDate() {
		return LocalDate.parse(date, formatter);
	}
	
	public void setPubId(Long pubId) {
		this.pubId = pubId;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
}
