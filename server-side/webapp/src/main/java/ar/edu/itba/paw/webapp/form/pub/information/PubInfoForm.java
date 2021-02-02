package ar.edu.itba.paw.webapp.form.pub.information;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class PubInfoForm {
	
	@NotNull
	@Digits(integer = 10, fraction = 0)
	@Exists(source = PubService.class)
	private Long pubId;
	
	@NotBlank
	@Size(max = 64)
	private String address;
	
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal price;

	public Long getPubId() {
		return pubId;
	}

	public void setPubId(Long pubId) {
		this.pubId = pubId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
