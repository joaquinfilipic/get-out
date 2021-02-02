package ar.edu.itba.paw.webapp.form.chat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class CreateUserMessageForm {
	
	@NotNull
	@Digits(integer = 10, fraction = 0)
	@Exists(source = UserService.class)
	private Long userId;
	
	@NotBlank
	@Size(max = 1024)
	private String message;
	
	public Long getUserId() {
		return userId;
	}
	
	public Message getMessage() {
		return new Message(message);
	}
	
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
	
}
