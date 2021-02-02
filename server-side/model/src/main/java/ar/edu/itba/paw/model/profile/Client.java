package ar.edu.itba.paw.model.profile;

import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.model.support.converter.ContentTypeConverter;
import ar.edu.itba.paw.model.support.converter.GenderConverter;
import com.fasterxml.jackson.annotation.JsonView;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Client")
@JsonView(View.Retrieval.class)
public class Client {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private User user;

	//@Enumerated(EnumType.STRING)
	@Convert(converter = GenderConverter.class)
	@Column(name = "gender", nullable = false, length = 6)
	private Gender gender;

	@JsonView(View.Retrieval.class)
	@Column(name = "avatar", nullable = true)
	private byte[] avatar;

	@JsonView(View.Retrieval.class)
	//@Enumerated(EnumType.STRING)
	@Convert(converter = ContentTypeConverter.class)
	@Column(name = "content", length = 16, nullable = true)
	private ContentType content;

	@Column(name = "bio", length = 256, nullable = false)
	private String bio;

	Client() {}

	public Client(final Builder builder) {
		id = builder.id;
		bio = builder.bio;
		user = builder.user;
		gender = builder.gender;
		avatar = builder.avatar;
		content = builder.content;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Gender getGender() {
		return gender;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public ContentType getContentType() {
		return content;
	}

	public String getBio() {
		return bio;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setGender(final Gender gender) {
		this.gender = gender;
	}

	public void setAvatar(final byte [] avatar) {
		this.avatar = avatar;
	}

	public void setContentType(final ContentType content) {
		this.content = content;
	}

	public void setBio(final String bio) {
		this.bio = bio;
	}

	public static class Builder {

		protected Long id;
		protected User user;
		protected Gender gender;
		protected byte[] avatar;
		protected ContentType content;
		protected String bio;

		public Builder() {}

		public Client build() {
			return new Client(this);
		}

		public Builder bio(final String bio) {
			this.bio = bio;
			return this;
		}

		public Builder user(final User user) {
			this.user = user;
			this.id = user.getId();
			return this;
		}

		public Builder gender(final Gender gender) {
			this.gender = gender;
			return this;
		}

		public Builder avatar(final byte[] avatar) {
			this.avatar = avatar;
			return this;
		}

		public Builder contentType(final ContentType content) {
			this.content = content;
			return this;
		}
	}
}
