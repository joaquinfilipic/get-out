package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.profile.User;

import java.util.Optional;

public interface UserDAO extends Mergeable<User> {

	public User create(final String username, final String password, final String name, final String mail);
	
	public Optional<User> findByKey(final String key, final Object value);

	public default Optional<User> findById(final Long id) {
		return findByKey("id", id);
	}

	public default Optional<User> findByUsername(final String username) {
		return findByKey("username", username);
	}
	
	public void delete(final Long id);
	
}
