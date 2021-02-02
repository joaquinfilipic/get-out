package ar.edu.itba.paw.webapp.form.pub.information;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class CoordinatesForm {
	
	@NotNull
	@Digits(integer = 10, fraction = 0)
	@Exists(source = PubService.class)
	private Long pubId;
	
	@NotNull
	@Digits(integer = 4, fraction = 8)
	private BigDecimal latitude;
	
	@NotNull
	@Digits(integer = 4, fraction = 8)
	private BigDecimal longitude;

	public Long getPubId() {
		return pubId;
	}

	public void setPubId(final Long pubId) {
		this.pubId = pubId;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
