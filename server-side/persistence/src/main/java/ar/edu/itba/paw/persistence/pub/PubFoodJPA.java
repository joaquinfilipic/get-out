package ar.edu.itba.paw.persistence.pub;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubFoodDAO;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubFood;

@Repository
public class PubFoodJPA implements PubFoodDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public PubFood create(final Long pubId, final String food, final Double price) {
		final PubFood pubFood = new PubFood.Builder()
				.pub(manager.find(Pub.class, pubId))
				.food(food)
				.price(price)
				.build();
		manager.persist(pubFood);
		manager.flush();
		return pubFood;
	}

	@Override
	public PubFood update(final PubFood pubFood) {
		return manager.merge(pubFood);
	}

	@Override
	public Optional<PubFood> findById(final Long id) {
		return Optional.ofNullable(manager.find(PubFood.class, id));
	}

	@Override
	public List<PubFood> listFoodByPub(final Long pubId) {
		return manager.createQuery(
				"FROM PubFood AS pubFood " +
						"WHERE pubFood.pub.id = :pubId " +
				"ORDER BY pubFood.id",
				PubFood.class)
						.setParameter("pubId", pubId)
						.getResultList();
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(pubFood -> {
			manager.remove(pubFood);
			manager.flush();
		});
	}

}
