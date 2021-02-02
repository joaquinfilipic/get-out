package ar.edu.itba.paw.webapp.support;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.edu.itba.paw.model.auth.AuthenticatedUser;

	/**
	* <p>Provee utilidades asociadas a la seguridad.</p>
	*/

public class SecuritySupport {

	public static Optional<AuthenticatedUser> getPrincipal() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated() && auth.getPrincipal() instanceof AuthenticatedUser) {
			return Optional.of((AuthenticatedUser) auth.getPrincipal());
		}
		else return Optional.empty();
	}
		
}
