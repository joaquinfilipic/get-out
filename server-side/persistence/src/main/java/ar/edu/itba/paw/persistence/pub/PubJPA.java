package ar.edu.itba.paw.persistence.pub;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.PubDAO;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;

@Repository
public class PubJPA implements PubDAO {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Pub create(final Long userId, final String name, final String description, 
			final LocalTime openTime, final Image image) {	
		final Pub pub = new Pub.Builder()
				.user(manager.find(User.class, userId))
				.name(name)
				.description(description)
				.openTime(openTime)
				.image(image.getData())
				.content(image.getContentType())
				.build();
		manager.persist(pub);
		manager.flush();
		return pub;
	}
	
	@Override
	public Pub update(final Pub pub) {
		return manager.merge(pub);
	}

	@Override
	public Optional<Pub> findById(final Long id) {
		return Optional.ofNullable(manager.find(Pub.class, id));
	}

	@Override
	public List<Pub> findPubs() {
		return manager.createQuery(
				"FROM Pub AS pub " +
				"ORDER BY pub.id",
				Pub.class).getResultList();
	}
	
	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(pub -> {
			manager.remove(pub);
			manager.flush();
		});
	}
	
}
