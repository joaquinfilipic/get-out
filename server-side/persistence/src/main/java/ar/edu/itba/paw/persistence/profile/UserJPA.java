package ar.edu.itba.paw.persistence.profile;

import ar.edu.itba.paw.interfaces.persistence.UserDAO;
import ar.edu.itba.paw.model.profile.User;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserJPA implements UserDAO {

	@PersistenceContext
	protected EntityManager manager;

	@Override
	public User create(final String username, final String password, final String name, final String mail) {
		final User user = new User.Builder()
				.username(username)
				.password(password)
				.name(name)
				.mail(mail)
				.build();
		manager.persist(user);
		manager.flush();
		return user;
	}

	@Override
	public Optional<User> findByKey(final String key, final Object value) {
		final TypedQuery<User> query = manager
				.createQuery("FROM User AS user WHERE user." + key + " = :value", User.class)
				.setParameter("value", value);
		final List<User> list = query.getResultList();
		if (list.isEmpty()) {
			return Optional.empty();
		}
		else return Optional.of(list.get(0));
	}

	@Override
	public Optional<User> findById(final Long id) {
		return Optional.ofNullable(manager.find(User.class, id));
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(user -> {
			manager.remove(user);
			manager.flush();
		});
	}

	@Override
	public User update(final User user) {
		return manager.merge(user);
	}
	
}
