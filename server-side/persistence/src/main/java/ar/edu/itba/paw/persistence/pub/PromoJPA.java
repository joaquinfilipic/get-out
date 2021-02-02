package ar.edu.itba.paw.persistence.pub;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PromoDAO;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.Promo;

@Repository
public class PromoJPA implements PromoDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Promo create(final Long pubId, final String name, final String description) {
		final Promo promo = new Promo.Builder()
				.pub(manager.find(Pub.class, pubId))
				.name(name)
				.description(description)
				.build();
		manager.persist(promo);
		manager.flush();
		return promo;
	}
	
	@Override
	public Promo update(final Promo promo) {
		return manager.merge(promo);
	}
	
	@Override
	public Optional<Promo> findById(final Long id) {
		return Optional.ofNullable(manager.find(Promo.class, id));
	}

	@Override
	public List<Promo> listPromosByPub(final Long pubId) {
		return manager.createQuery(
				"FROM Promo AS promo " +
						"WHERE promo.pub.id = :pubId " +
				"ORDER BY promo.id",
				Promo.class)
						.setParameter("pubId", pubId)
						.getResultList();
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(promo -> {
			manager.remove(promo);
			manager.flush();
		});
	}
	
}
