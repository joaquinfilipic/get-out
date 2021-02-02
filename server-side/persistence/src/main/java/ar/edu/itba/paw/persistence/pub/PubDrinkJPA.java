package ar.edu.itba.paw.persistence.pub;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubDrinkDAO;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubDrink;

@Repository
public class PubDrinkJPA implements PubDrinkDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public PubDrink create(final Long pubId, final String drink, final Double price) {
		final PubDrink pubDrink = new PubDrink.Builder()
				.pub(manager.find(Pub.class, pubId))
				.drink(drink)
				.price(price)
				.build();
		manager.persist(pubDrink);
		manager.flush();
		return pubDrink;
	}

	@Override
	public PubDrink update(final PubDrink pubDrink) {
		return manager.merge(pubDrink);
	}

	@Override
	public Optional<PubDrink> findById(final Long id) {
		return Optional.ofNullable(manager.find(PubDrink.class, id));
	}

	@Override
	public List<PubDrink> listDrinksByPub(final Long pubId) {
		return manager.createQuery(
				"FROM PubDrink AS pubDrink " +
						"WHERE pubDrink.pub.id = :pubId " +
				"ORDER BY pubDrink.id",
				PubDrink.class)
						.setParameter("pubId", pubId)
						.getResultList();
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(pubDrink -> {
			manager.remove(pubDrink);
			manager.flush();
		});
	}

}
