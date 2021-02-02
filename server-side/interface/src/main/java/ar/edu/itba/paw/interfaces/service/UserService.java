package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.functional.Deduplicator;
import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.profile.User;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public interface UserService extends Deduplicator, ExistenceCheckable {

	public User create(final String username, final String password, final String name, final String mail);
	
	public User login(final String username, final String password);

	public boolean updateProfile(final Long id, final String name, final String username, final String mail);
	
	public boolean updatePassword(final Long id, final String password);

	public void logout(final HttpServletRequest request);

	public void delete(final Long id);

	public default boolean isDuplicated(final Optional<Long> id, final String key, final Object value) {
		return findByKey(key, value)
				.map(user -> !id.isPresent() || !user.getId().equals(id.get()))
				.orElse(false);
	}

	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}
	
	
	

	// Modificar, revisar, etc.
	public Optional<User> findById(final Long id);

	public Optional<User> findByUsername(final String username);

	public Optional<User> findByKey(final String key, final Object value);

}
