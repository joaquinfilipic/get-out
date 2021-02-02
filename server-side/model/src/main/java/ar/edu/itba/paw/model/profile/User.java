package ar.edu.itba.paw.model.profile;

import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.model.relation.Likes;
import ar.edu.itba.paw.model.relation.Reject;
import ar.edu.itba.paw.model.support.View;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Users", uniqueConstraints = {
	@UniqueConstraint(columnNames = "username"),
	@UniqueConstraint(columnNames = "mail")
})
@JsonView(View.Public.class)
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
	@SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
	protected Long id;

	@Column(length = 32, nullable = false, unique = true)
	protected String username;

	@JsonView(View.Private.class)
	@Column(length = 60, nullable = false)
	protected String password;

	@Column(length = 64, nullable = false)
	protected String name;

	@JsonView(View.Retrieval.class)
	@Column(length = 254, nullable = false, unique = true)
	protected String mail;
	
	// Only for hibernate
	
	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Client client;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<Attendance> attendances;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<PubMessage> pubMessages;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	private Set<Likes> sendedLikes;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
	private Set<Likes> receivedLikes;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	private Set<Reject> sendedRejects;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
	private Set<Reject> receivedRejects;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Pub> pubs;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	private Set<UserMessage> sendedMessages;
	
	@JsonIgnore
	@OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
	private Set<UserMessage> receivedMessages;
	
	public User() {}

	public User(final Builder builder) {
		id = builder.id;
		username = builder.username;
		password = builder.password;
		name = builder.name;
		mail = builder.mail;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return mail;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setMail(final String mail) {
		this.mail = mail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder {

		protected Long id;
		protected String username;
		protected String password;
		protected String name;
		protected String mail;

		public Builder() {}

		public User build() {
			return new User(this);
		}

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}

		public Builder username(final String username) {
			this.username = username;
			return this;
		}

		public Builder password(final String password) {
			this.password = password;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Builder mail(final String mail) {
			this.mail = mail;
			return this;
		}
	}
}
