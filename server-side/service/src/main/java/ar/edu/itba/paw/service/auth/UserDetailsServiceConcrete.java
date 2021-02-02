package ar.edu.itba.paw.service.auth;

import ar.edu.itba.paw.interfaces.service.ClientService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.auth.AuthenticatedUser;
import ar.edu.itba.paw.model.profile.Client;
import java.util.Arrays;
import javax.inject.Inject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceConcrete implements UserDetailsService {

	protected static final String ROLE = "ROLE_USER";

	protected final GrantedAuthority authority;
	protected final ClientService clientService;
	protected final UserService userService;

	@Inject
	public UserDetailsServiceConcrete(
			final UserService userService,
			final ClientService clientService) {
		this.authority = new SimpleGrantedAuthority(ROLE);
		this.clientService = clientService;
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		return userService.findByUsername(username)
				.map(user -> {
					final Client client = clientService.findById(user.getId()).get();
					return new AuthenticatedUser(user, client, Arrays.asList(authority));
				})
				.orElseThrow(() -> new UsernameNotFoundException(
					"No existe un usuario con el nombre: " + username)
				);
	}
}
