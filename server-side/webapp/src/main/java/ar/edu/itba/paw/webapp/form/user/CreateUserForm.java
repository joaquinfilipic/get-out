package ar.edu.itba.paw.webapp.form.user;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.validation.Valid;

public class CreateUserForm {

	@Valid
	@JsonUnwrapped
	private UserForm userForm;
	
	@Valid
	@JsonUnwrapped
	private PasswordForm passwordForm;

	public CreateUserForm() {
		userForm = new UserForm();
		passwordForm = new PasswordForm();
	}

	public UserForm getUserForm() {
		return userForm;
	}

	public void setUserForm(final UserForm userForm) {
		this.userForm = userForm;
	}
	
	public PasswordForm getPasswordForm() {
		return passwordForm;
	}
	
	public void setPasswordForm(final PasswordForm passwordForm) {
		this.passwordForm = passwordForm;
	}

}
