package ar.edu.itba.paw.webapp.form.user;

import javax.validation.constraints.NotBlank;

public class LoginForm {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
	
	public LoginForm username(final String username) {
		this.username = username;
		return this;
	}
	
	public LoginForm password(final String password) {
		this.password = password;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
