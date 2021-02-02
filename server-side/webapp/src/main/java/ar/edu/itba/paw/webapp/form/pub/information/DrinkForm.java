package ar.edu.itba.paw.webapp.form.pub.information;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class DrinkForm {
	
	@NotNull
	@Digits(integer = 10, fraction = 0)
	@Exists(source = PubService.class)
	private Long pubId;
	
	@NotBlank
	@Size(max = 32)
	private String drink;
	
	@NotNull
	@Digits(integer = 4, fraction = 8)
	private BigDecimal price;

	public Long getPubId() {
		return pubId;
	}

	public void setPubId(Long pubId) {
		this.pubId = pubId;
	}

	public String getDrink() {
		return drink;
	}

	public void setDrink(String drink) {
		this.drink = drink;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
