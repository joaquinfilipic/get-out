package ar.edu.itba.paw.webapp.form.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordForm {
	
	@NotBlank
	@Size(min = 10)
	private String password;
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(final String password) {
		this.password = password;
	}

}
