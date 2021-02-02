package ar.edu.itba.paw.model.relation;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.fasterxml.jackson.annotation.JsonView;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.model.support.converter.LocalDateConverter;

@Entity
@Table(name = "Attendance", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"pubId", "userId", "date"})
})
@JsonView(View.Public.class)
public class Attendance {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_id_seq")
	@SequenceGenerator(sequenceName = "attendance_id_seq", name = "attendance_id_seq", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	@Convert(converter = LocalDateConverter.class)
	private LocalDate date;
	
	Attendance() {}
	
	public Attendance(final Builder builder) {
		id = builder.id;
		pub = builder.pub;
		date = builder.date;
		user = builder.user;
	}
	
	public Long getId() {	
		return id;
	}
	
	public Pub getPub() {
		return pub;
	}
	
	public User getUser() {
		return user;
	}

	public LocalDate getDate() {	
		return date;
	}
	
	public void setPub(final Pub pub) {
		this.pub = pub;
	}
	
	public void setUser(final User user) {
		this.user = user;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}
	
	public static class Builder {
		
		public Long id;
		public Pub pub;
		public User user;
		public LocalDate date;
		
		public Builder() {}
		
		public Attendance build() {
			return new Attendance(this);
		}
		
		public Builder id(final Long id) {
			this.id = id;
			return this;
		}
		
		public Builder pub(final Pub pub) {
			this.pub = pub;
			return this;
		}
		
		public Builder user(final User user) {
			this.user = user;
			return this;
		}
		
		public Builder date(final LocalDate date) {
			this.date = date;
			return this;
		}
		
	}
}
