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
@Table(name = "Promo")
public class Promo {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promo_id_seq")
	@SequenceGenerator(sequenceName = "promo_id_seq", name = "promo_id_seq", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;
	
	@Column(length = 32, nullable = false)
	private String name;
	
	@Column(length = 128, nullable = false)
	private String description;
	
	Promo() {}
	
	public Promo(final Builder builder) {
		
		id = builder.id;
		pub = builder.pub;
		name = builder.name;
		description = builder.description;
	}
	
	public Long getId() {
		
		return id;
	}
		
	public Long getPubId() {
		
		return pub.getId();
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setId(final Long id) {
		
		this.id = id;
	}
	
	public void setPub(final Pub pub) {
		
		this.pub = pub;
	}
	
	public void setName(final String name) {
		
		this.name = name;
	}
	
	public void setDescription(final String description) {
		
		this.description = description;
	}
	
	public static class Builder {
		
		private Long id;
		private Pub pub;
		private String name;
		private String description;
		
		public Builder() {}
		
		public Promo build() {
			
			return new Promo(this);
		}
		
		public Builder id(final Long id) {
			
			this.id = id;
			return this;
		}
		
		public Builder pub(final Pub pub) {
			
			this.pub = pub;
			return this;
		}
		
		public Builder name(final String name) {
			
			this.name = name;
			return this;
		}
		
		public Builder description(final String description) {
			
			this.description = description;
			return this;
		}
	}
	
}