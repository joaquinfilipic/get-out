package ar.edu.itba.paw.model.pub.information;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.edu.itba.paw.model.pub.Pub;

@Entity
@Table(name = "PubFood")
public class PubFood {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pubfood_id_seq")
	@SequenceGenerator(sequenceName = "pubfood_id_seq", name = "pubfood_id_seq", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;
	
	@Column(length = 32, nullable = false)
	private String food;
	
	@Column(precision = 10, scale = 2, nullable = false)
	private Double price;
	
	PubFood() {}
	
	public PubFood(final Builder builder) {
		id = builder.id;
		pub = builder.pub;
		food = builder.food;
		price = builder.price;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getPubId() {
		return pub.getId();
	}
	
	public String getFood() {
		return food;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setPub(final Pub pub) {
		this.pub = pub;
	}
	
	public void setFood(final String food) {
		this.food = food;
	}
	
	public void setPrice(final Double price) {
		this.price = price;
	}
	
	public static class Builder {
		
		private Long id;
		private Pub pub;
		private String food;
		private Double price;
		
		public Builder() {}
		
		public PubFood build() {
			return new PubFood(this);
		}
		
		public Builder id(final Long id) {	
			this.id = id;
			return this;
		}
		
		public Builder pub(final Pub pub) {	
			this.pub = pub;
			return this;
		}
		
		public Builder food(final String food) {
			this.food = food;
			return this;
		}
		
		public Builder price(final Double price) {	
			this.price = price;
			return this;
		}
	}
}
