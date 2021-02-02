package ar.edu.itba.paw.persistence.pub;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.CoordinatesDAO;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.Coordinates;

@Repository
public class CoordinatesJPA implements CoordinatesDAO {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Coordinates create(final Long pubId, final Double latitude, final Double longitude) {
		final Coordinates coordinates = new Coordinates.Builder()
				.pub(manager.find(Pub.class, pubId))
				.latitude(latitude)
				.longitude(longitude)
				.build();	
		manager.persist(coordinates);
		manager.flush();	
		return coordinates;
	}

	@Override
	public Coordinates update(final Coordinates coordinates) {
		return manager.merge(coordinates);
	}

	@Override
	public Optional<Coordinates> findById(final Long id) {
		return Optional.ofNullable(manager.find(Coordinates.class, id));
	}
	
	@Override
	public Optional<Coordinates> findByPubId(final Long id) {
		final TypedQuery<Coordinates> query = manager.createQuery(
				"FROM Coordinates AS coordinates " +
						"WHERE coordinates.pub.id = :id",
				Coordinates.class)
						.setParameter("id", id);
		final List<Coordinates> list = query.getResultList();
		if (list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(list.get(0));
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(coordinates -> {
			manager.remove(coordinates);
			manager.flush();
		});
	}

}
