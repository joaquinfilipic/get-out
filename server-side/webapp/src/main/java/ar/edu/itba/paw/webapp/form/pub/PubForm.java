package ar.edu.itba.paw.webapp.form.pub;

import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PubForm {
	
	@NotBlank
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[a-zA-Z0-9_ ]*")
	private String name;
	
	@NotBlank
	@Size(max = 256)
	private String description;
	
	@Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$")
	private String openTime;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public LocalTime getOpenTime() {
		return LocalTime.parse(openTime);
	}

	public void setOpenTime(final String openTime) {
		this.openTime = openTime;
	}

}
