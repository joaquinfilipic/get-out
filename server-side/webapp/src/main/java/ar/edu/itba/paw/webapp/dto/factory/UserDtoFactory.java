package ar.edu.itba.paw.webapp.dto.factory;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.webapp.dto.model.profile.UserDto;

public class UserDtoFactory {
	
	public static UserDto buildFromUser(final User user) {
		return new UserDto()
				.id(user.getId())
				.username(user.getUsername())
				.name(user.getName())
				.mail(user.getMail());
	}

}
