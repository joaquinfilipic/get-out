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
@Table(name = "Likes", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"senderId", "receiverId", "pubId", "date"})
})
@JsonView(View.Public.class)
public class Likes {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "likes_id_seq")
	@SequenceGenerator(sequenceName = "likes_id_seq", name = "likes_id_seq", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "senderId", referencedColumnName = "id", nullable = false)
	private User sender;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "receiverId", referencedColumnName = "id", nullable = false)
	private User receiver;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	private Pub pub;

	@Column(nullable = false)
	@Convert(converter = LocalDateConverter.class)
	private LocalDate date;
	
	public Likes() {}

	public Likes(final Builder builder) {
		id = builder.id;
		sender = builder.sender;
		receiver = builder.receiver;
		pub = builder.pub;
		date = builder.date;
	}
	
	public Long getId() {
		return id;
	}
	
	public User getSender() {
		return sender;
	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public Pub getPub() {
		return pub;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setSender(final User sender) {
		this.sender = sender;
	}
	
	public void setReceiver(final User receiver) {
		this.receiver = receiver;
	}
	
	public void setPub(final Pub pub) {
		this.pub = pub;
	}
	
	public void setDate(final LocalDate date) {
		this.date = date;
	}
	
	public static class Builder {
		
		private Long id;
		private User sender;
		private User receiver;
		private Pub pub;
		private LocalDate date;
		
		public Builder() {}
		
		public Likes build() {
			return new Likes(this);
		}
		
		public Builder id(final Long id) {
			this.id = id;
			return this;
		}
		
		public Builder sender(final User sender) {
			this.sender = sender;
			return this;
		}
		
		public Builder receiver(final User receiver) {
			this.receiver = receiver;
			return this;
		}
		
		public Builder pub(final Pub pub) {
			this.pub = pub;
			return this;
		}
		
		public Builder date(final LocalDate date) {
			this.date = date;
			return this;
		}
	}
}