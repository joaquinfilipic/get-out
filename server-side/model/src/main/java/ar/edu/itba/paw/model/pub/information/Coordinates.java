package ar.edu.itba.paw.model.pub.information;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.edu.itba.paw.model.pub.Pub;

@Entity
@Table(name = "Coordinates", uniqueConstraints = {
	@UniqueConstraint(columnNames = "pubId")
})
public class Coordinates {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_id_seq")
	@SequenceGenerator(sequenceName = "coordinates_id_seq", name = "coordinates_id_seq", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;
	
	@Column(precision = 12, scale = 8, nullable = false)
	private Double latitude;
	
	@Column(precision = 12, scale = 8, nullable = false)
	private Double longitude;
	
	Coordinates() {}
	
	public Coordinates(final Builder builder) {
		
		id = builder.id;
		pub = builder.pub;
		latitude = builder.latitude;
		longitude = builder.longitude;
	}
	
	public Long getId() {
		
		return id;
	}

	public Long getPubId() {
		
		return pub.getId();
	}

	public Double getLatitude() {
		
		return latitude;
	}

	public Double getLongitude() {
		
		return longitude;
	}
	
	public void setId(final Long id) {
		
		this.id = id;
	}
	
	public void setPub(final Pub pub) {
		
		this.pub = pub;
	}
	
	public void setLatitude(final Double latitude) {
		
		this.latitude = latitude;
	}
	
	public void setLongitude(final Double longitude) {
		
		this.longitude = longitude;
	}
	
	public static class Builder {
		
		private Long id;
		private Pub pub;
		private Double latitude;
		private Double longitude;
		
		public Builder() {}
		
		public Coordinates build() {
			
			return new Coordinates(this);
		}
		
		public Builder id(final Long id) {
			
			this.id = id;
			return this;
		}
		
		public Builder pub(final Pub pub) {
			
			this.pub = pub;
			return this;
		}
		
		public Builder latitude(final Double latitude) {
			
			this.latitude = latitude;
			return this;
		}
		
		public Builder longitude(final Double longitude) {
			
			this.longitude = longitude;
			return this;
		}
	}
}
