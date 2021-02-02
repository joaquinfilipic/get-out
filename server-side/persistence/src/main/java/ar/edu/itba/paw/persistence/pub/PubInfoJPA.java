package ar.edu.itba.paw.persistence.pub;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubInfoDAO;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubInfo;

@Repository
public class PubInfoJPA implements PubInfoDAO {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public PubInfo create(final Long pubId, final String address, final Double price) {
		final PubInfo pubInfo = new PubInfo.Builder()
				.pub(manager.find(Pub.class, pubId))
				.address(address)
				.price(price)
				.build();
		manager.persist(pubInfo);
		manager.flush();
		return pubInfo;
	}
	
	@Override
	public PubInfo update(final PubInfo pubInfo) {
		return manager.merge(pubInfo);
	}
	
	@Override
	public Optional<PubInfo> findById(final Long id) {
		return Optional.ofNullable(manager.find(PubInfo.class, id));
	}
	
	@Override
	public Optional<PubInfo> findByPubId(final Long id) {
		final TypedQuery<PubInfo> query = manager.createQuery(
				"FROM PubInfo AS pubInfo " +
						"WHERE pubInfo.pub.id = :id",
				PubInfo.class)
						.setParameter("id", id);
		final List<PubInfo> list = query.getResultList();
		if (list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(list.get(0));
	}
	
	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(pubInfo -> {
			manager.remove(pubInfo);
			manager.flush();
		});
	}
	
}
