package ar.edu.itba.paw.webapp.form.user;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.profile.Gender;
import ar.edu.itba.paw.webapp.constraint.Unique;
import ar.edu.itba.paw.webapp.constraint.Enum;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {

	@NotBlank
	@Size(min = 6, max = 32)
	@Unique(key = "username", source = UserService.class)
	@Pattern(regexp = "[a-zA-Z]([-.]?[a-zA-Z0-9]+)*")
	private String username;

	@NotBlank
	@Size(min = 1, max = 64)
	private String name;

	@Email
	@NotBlank
	@Unique(key = "mail", source = UserService.class)
	@Size(min = 5, max = 254)
	private String mail;
	
	@Size(min = 0, max = 160)
	private String bio;
	
	@Enum(enumClass = Gender.class, ignoreCase = true)
	private String gender;
	
	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return mail;
	}
	
	public String getBio() {
		return bio;
	}
	
	public Gender getGender() {
		if (gender == null) {
			return Gender.OTHER;
		}
		return Gender.fromGender(gender);
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setMail(final String mail) {
		this.mail = mail;
	}
	
	public void setBio(final String bio) {
		this.bio = bio;
	}
	
	public void setGender(final String gender) {
		this.gender = gender;
	}
	
}
