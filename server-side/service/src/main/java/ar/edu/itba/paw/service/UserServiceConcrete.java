package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.persistence.UserDAO;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.profile.User;
import java.util.Locale;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceConcrete implements UserService {

	protected final AuthenticationManager manager;
	protected final PasswordEncoder encoder;
	protected final UserDAO userDAO;

	@Inject
	public UserServiceConcrete(
			final AuthenticationManager manager,
			final PasswordEncoder encoder,
			final UserDAO userDAO) {
		this.manager = manager;
		this.encoder = encoder;
		this.userDAO = userDAO;
	}

	@Override
	public User create(final String username, final String password, final String name, final String mail) {
		return userDAO.create(
				username.trim().toLowerCase(Locale.ROOT),
				encoder.encode(password),
				name.trim(),
				mail.trim().toLowerCase(Locale.ROOT));
	}

	@Override
	public User login(final String username, final String password) {
		manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		final User user = findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Las credenciales son inválidas."));
		return user;
	}

	@Override
	public void logout(final HttpServletRequest request) {
		new SecurityContextLogoutHandler().logout(request, null, null);
	}
	
	@Override
	public boolean updateProfile(final Long id, final String name,
			final String username, final String mail) {
		final Optional<User> result = findById(id);
		final boolean updated = !result
				.filter(user -> user.getName().equals(name))
				.filter(user -> user.getUsername().equals(username))
				.filter(user -> user.getMail().equals(mail))
				.isPresent();
		result.ifPresent(user -> {
			user.setMail(mail);
			user.setName(name);
			user.setUsername(username);
			userDAO.update(user);
		});
		return updated;
	}
	
	@Override
	public boolean updatePassword(final Long id, final String password) {
		final Optional<User> result = findById(id);
		return result.map(user -> {
			user.setPassword(encoder.encode(password));
			userDAO.update(user);
			return true;
		})
		.orElse(false);
	}

	@Override
	public void delete(final Long id) {
		userDAO.delete(id);
	}

	@Override
	public Optional<User> findByKey(final String key, final Object value) {
		return userDAO.findByKey(key, value);
	}

	@Override
	public Optional<User> findById(final Long id) {
		return userDAO.findById(id);
	}

	@Override
	public Optional<User> findByUsername(final String username) {
		return userDAO.findByUsername(username);
	}

}
