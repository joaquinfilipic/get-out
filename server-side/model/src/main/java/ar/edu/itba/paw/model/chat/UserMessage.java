package ar.edu.itba.paw.model.chat;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.model.support.converter.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(name = "UserMessage")
@JsonView(View.Public.class)
public class UserMessage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usermessage_id_seq")
	@SequenceGenerator(sequenceName = "usermessage_id_seq", name = "usermessage_id_seq", allocationSize = 1)
	protected Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "senderId", referencedColumnName = "id", nullable = false)
	protected User sender;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "recipientId", referencedColumnName = "id", nullable = false)
	protected User recipient;

	@Column(nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	protected LocalDateTime timestamp;

	@Column(name = "message", length = 1024, nullable = false)
	@JsonView(View.Retrieval.class)
	protected String message;

	UserMessage() {
	}

	public UserMessage(final Builder builder) {
		id = builder.id;
		sender = builder.sender;
		recipient = builder.recipient;
		timestamp = builder.timestamp;
		message = builder.message;
	}

	@JsonGetter("timestamp")
	public String getStringTimestamp() {
		return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	@JsonGetter("senderId")
	public Long getSenderId() {
		return sender.getId();
	}

	@JsonGetter("recipientId")
	public Long getRecipientId() {
		return recipient.getId();
	}

	@JsonGetter("senderUsername")
	public String getSenderUsername() {
		return sender.getUsername();
	}

	@JsonGetter("recipientUsername")
	public String getRecipientUsername() {
		return recipient.getUsername();
	}

	public Long getId() {
		return id;
	}

	public User getSender() {
		return sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setSender(final User sender) {
		this.sender = sender;
	}

	public void setRecipient(final User recipient) {
		this.recipient = recipient;
	}

	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public static class Builder {

		protected Long id;
		protected User sender;
		protected User recipient;
		protected LocalDateTime timestamp;
		protected String message;

		public Builder() {}

		public UserMessage build() {
			return new UserMessage(this);
		}

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder sender(final User sender) {
			this.sender = sender;
			return this;
		}

		public Builder recipient(final User recipient) {
			this.recipient = recipient;
			return this;
		}

		public Builder timestamp(final LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder message(final String message) {
			this.message = message;
			return this;
		}
	}
}
