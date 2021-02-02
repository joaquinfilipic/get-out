package ar.edu.itba.paw.interfaces.auth;

import ar.edu.itba.paw.model.profile.User;
import org.springframework.security.core.Authentication;

public interface JWTAuthenticationService {

	public String createToken(final User user);
	public Authentication getAuthentication(final String token);
}
