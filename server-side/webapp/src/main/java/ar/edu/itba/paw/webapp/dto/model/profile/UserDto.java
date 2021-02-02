package ar.edu.itba.paw.webapp.dto.model.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserDto {
	
	private Long id;
	private String username;
	private String password;
	private String name;
	private String mail;
	
	public UserDto() {}
	
	public UserDto id(final Long id) {
		this.id = id;
		return this;
	}
	
	public UserDto username(final String username) {
		this.username = username;
		return this;
	}
	
	public UserDto password(final String password) {
		this.password = password;
		return this;
	}
	
	public UserDto name(final String name) {
		this.name = name;
		return this;
	}
	
	public UserDto mail(final String mail) {
		this.mail = mail;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(final String mail) {
		this.mail = mail;
	}

}
