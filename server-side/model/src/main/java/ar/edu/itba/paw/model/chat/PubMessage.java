package ar.edu.itba.paw.model.chat;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.model.support.converter.LocalDateConverter;
import ar.edu.itba.paw.model.support.converter.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

@Entity
@Table(name = "PubMessage")
@JsonView(View.Public.class)
public class PubMessage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pubmessage_id_seq")
	@SequenceGenerator(sequenceName = "pubmessage_id_seq", name = "pubmessage_id_seq", allocationSize = 1)
	protected Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
	protected User user;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pubId", referencedColumnName = "id", nullable = false)
	protected Pub pub;

	@Column(nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	protected LocalDateTime timestamp;

	@Column(nullable = false)
	@Convert(converter = LocalDateConverter.class)
	@JsonView(View.Retrieval.class)
	protected LocalDate date;

	@Column(name = "message", length = 1024, nullable = false)
	@JsonView(View.Retrieval.class)
	protected String message;

	PubMessage() {
	}

	public PubMessage(final Builder builder) {
		id = builder.id;
		user = builder.user;
		pub = builder.pub;
		timestamp = builder.timestamp;
		date = builder.date;
		message = builder.message;
	}

	@JsonGetter("timestamp")
	public String getStringTimestamp() {
		return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	@JsonGetter("date")
	public String getStringDate() {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@JsonGetter("userId")
	public Long getUserId() {
		return user.getId();
	}

	@JsonGetter("pubId")
	public Long getPubId() {
		return pub.getId();
	}

	@JsonGetter("senderUsername")
	public String getUsername() {
		return user.getUsername();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Pub getPub() {
		return pub;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setPub(final Pub pub) {
		this.pub = pub;
	}

	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public static class Builder {

		protected Long id;
		protected User user;
		protected Pub pub;
		protected LocalDateTime timestamp;
		protected LocalDate date;
		protected String message;

		public Builder() {}

		public PubMessage build() {
			return new PubMessage(this);
		}

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder user(final User user) {
			this.user = user;
			return this;
		}

		public Builder pub(final Pub pub) {
			this.pub = pub;
			return this;
		}

		public Builder timestamp(final LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder date(final LocalDate date) {
			this.date = date;
			return this;
		}

		public Builder message(final String message) {
			this.message = message;
			return this;
		}
	}
}
