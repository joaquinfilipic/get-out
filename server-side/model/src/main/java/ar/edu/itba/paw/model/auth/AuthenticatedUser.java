package ar.edu.itba.paw.model.auth;

import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.profile.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedUser
	extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
	private final Client client;
	private final User user;

	public AuthenticatedUser(final User user, final Client client,
			final Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), authorities);
		this.client = client;
		this.user = user;
	}

	public Client getClient() {
		return client;
	}

	public Long getId() {
		return user.getId();
	}

	public User getUser() {
		return user;
	}
}
