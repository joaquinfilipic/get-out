package ar.edu.itba.paw.model.pub;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.information.Coordinates;
import ar.edu.itba.paw.model.pub.information.Promo;
import ar.edu.itba.paw.model.pub.information.PubDrink;
import ar.edu.itba.paw.model.pub.information.PubFood;
import ar.edu.itba.paw.model.pub.information.PubInfo;
import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.model.relation.Likes;
import ar.edu.itba.paw.model.relation.Reject;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.model.support.converter.ContentTypeConverter;
import ar.edu.itba.paw.model.support.converter.LocalTimeConverter;

@Entity
@Table(name = "Pub", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name"})
})
@JsonView(View.Public.class)
public class Pub {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pub_id_seq")
	@SequenceGenerator(sequenceName = "pub_id_seq", name = "pub_id_seq", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
	private User user;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(length = 256, nullable = false)
	private String description;

	@Convert(converter = LocalTimeConverter.class)
	@Column(length = 5, nullable = false)
	private LocalTime openTime;

	@JsonView(View.Retrieval.class)
	@Column(name = "image", nullable = false)
	private byte[] image;

	@JsonView(View.Retrieval.class)
	//@Enumerated(EnumType.STRING)
	@Convert(converter = ContentTypeConverter.class)
	@Column(name = "content", length = 16, nullable = false)
	private ContentType contentType;
	
	// For hibernate
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<Attendance> attendances;
	
	@JsonIgnore
	@OneToOne(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Coordinates coordinates;
	
	@JsonIgnore
	@OneToOne(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private PubInfo pubInfo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<Promo> promos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<PubDrink> drinks;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
	private Set<PubFood> foods;
	
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
	
	Pub() {
	}

	public Pub(final Builder builder) {

		id = builder.id;
		user = builder.user;
		name = builder.name;
		description = builder.description;
		openTime = builder.openTime;
		image = builder.image;
		contentType = builder.contentType;
	}

	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public byte[] getImage() {
		return image;
	}
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setUser(final User user) {
		this.user = user;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setOpenTime(final LocalTime openTime) {
		this.openTime = openTime;
	}

	public void setImage(final byte[] image) {
		this.image = image;
	}

	public void setContentType(final ContentType contentType) {
		this.contentType = contentType;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (this.getClass() != other.getClass())
			return false;
		Pub pub = (Pub) other;
		return pub.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return id.intValue();
	}

	public static class Builder {

		private Long id;
		private User user;
		private String name;
		private String description;
		private LocalTime openTime;
		private byte[] image;
		private ContentType contentType;

		public Builder() {}

		public Pub build() {
			return new Pub(this);
		}

		public Builder id(final Long id) {
			this.id = id;
			return this;
		}
		
		public Builder user(final User user) {
			this.user = user;
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

		public Builder openTime(final LocalTime openTime) {
			this.openTime = openTime;
			return this;
		}

		public Builder image(final byte[] image) {
			this.image = image;
			return this;
		}

		public Builder content(final ContentType contentType) {
			this.contentType = contentType;
			return this;
		}
	}
}
