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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.edu.itba.paw.model.pub.Pub;

@Entity
@Table(name = "PubInfo", uniqueConstraints = {
	@UniqueConstraint(columnNames = "pubId")
})
public class PubInfo {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pubinfo_id_seq")
	@SequenceGenerator(sequenceName = "pubinfo_id_seq", name = "pubinfo_id_seq", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;
	
	@Column(length = 64, nullable = false)
	private String address;
	
	@Column(precision = 10, scale = 2, nullable = false)
	private Double price;
	
	PubInfo() {}
	
	public PubInfo(final Builder builder) {
		
		id = builder.id;
		pub = builder.pub;
		address = builder.address;
		price = builder.price;
	}
	
	public Long getId() {
		
		return id;
	}
	
	public Long getPubId() {
		
		return pub.getId();
	}
	
	public String getAddress() {
		
		return address;
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
	
	public void setAddress(final String address) {
		
		this.address = address;
	}
	
	public void setPrice(final Double price) {
		
		this.price = price;
	}
	
	public static class Builder {
		
		private Long id;
		private Pub pub;
		private String address;
		private Double price;
		
		public Builder() {}
		
		public PubInfo build() {
			
			return new PubInfo(this);
		}
		
		public Builder id(final Long id) {
			
			this.id = id;
			return this;
		}
		
		public Builder pub(final Pub pub) {
			
			this.pub = pub;
			return this;
		}
		
		public Builder address(final String address) {
			
			this.address = address;
			return this;
		}
		
		public Builder price(final Double price) {
			
			this.price = price;
			return this;
		}
	}
}
