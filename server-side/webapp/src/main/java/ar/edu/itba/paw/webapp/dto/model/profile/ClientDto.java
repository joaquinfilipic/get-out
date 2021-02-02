package ar.edu.itba.paw.webapp.dto.model.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.profile.Gender;

@JsonInclude(Include.NON_NULL)
public class ClientDto {
	
	private Long id;
	private UserDto user;
	private Gender gender;
	private byte[] avatar;
	private ContentType content;
	private String bio;
	
	public ClientDto() {}
	
	public ClientDto id(final Long id) {
		this.id = id;
		return this;
	}
	
	public ClientDto user(final UserDto user) {
		this.user = user;
		return this;
	}
	
	public ClientDto gender(final Gender gender) {
		this.gender = gender;
		return this;
	}
	
	public ClientDto avatar(final byte[] avatar) {
		this.avatar = avatar;
		return this;
	}
	
	public ClientDto content(final ContentType content) {
		this.content = content;
		return this;
	}
	
	public ClientDto bio(final String bio) {
		this.bio = bio;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(final UserDto user) {
		this.user = user;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(final Gender gender) {
		this.gender = gender;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(final byte[] avatar) {
		this.avatar = avatar;
	}

	public ContentType getContent() {
		return content;
	}

	public void setContent(final ContentType content) {
		this.content = content;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(final String bio) {
		this.bio = bio;
	}

}
