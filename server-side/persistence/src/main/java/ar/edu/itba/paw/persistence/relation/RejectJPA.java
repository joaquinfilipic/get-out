package ar.edu.itba.paw.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.relation.RejectDAO;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.relation.Reject;

@Repository
public class RejectJPA implements RejectDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Reject create(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		final Reject reject = new Reject.Builder()
				.sender(manager.find(User.class, senderId))
				.receiver(manager.find(User.class, receiverId))
				.pub(manager.find(Pub.class, pubId))
				.date(date)
				.build();
		manager.persist(reject);
		manager.flush();
		return reject;
	}

	@Override
	public Optional<Reject> findById(final Long id) {
		return Optional.ofNullable(manager.find(Reject.class, id));
	}
	
	public Optional<Reject> findBySenderAndReceiverAndPubAndDate(final Long senderId, final Long receiverId, 
			final Long pubId, final LocalDate date) {
		try {
			return Optional.of(manager.createQuery(
					"FROM Reject AS reject " +
							"WHERE reject.sender.id = :senderId " +
							"AND reject.receiver.id = :receiverId " +
							"AND reject.pub.id = :pubId " +
							"AND reject.date = :date",
					Reject.class)
						.setParameter("senderId", senderId)
						.setParameter("receiverId", receiverId)
						.setParameter("pubId", pubId)
						.setParameter("date", date)
						.getSingleResult());
		} catch (NoResultException exception) {
			return Optional.empty();
		}
	}

	@Override
	public List<Reject> listBySenderAndPubAndDate(final Long senderId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Reject AS reject " +
						"WHERE reject.sender.id = :senderId " +
						"AND reject.pub.id = :pubId " +
						"AND date = :date",
				Reject.class)
						.setParameter("senderId", senderId)
						.setParameter("pubId", pubId)
						.setParameter("date", date)
						.getResultList();
	}

	@Override
	public List<Reject> listByReceiverAndPubAndDate(final Long receiverId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Reject AS reject " +
						"WHERE reject.receiver.id = :receiverId " +
						"AND reject.pub.id = :pubId " +
						"AND date = :date",
				Reject.class)
						.setParameter("receiverId", receiverId)
						.setParameter("pubId", pubId)
						.setParameter("date", date)
						.getResultList();
	}

	@Override
	public List<Reject> listBySender(final Long id) {
		return manager.createQuery(
				"FROM Reject AS reject " +
						"WHERE reject.sender.id = :id",
				Reject.class)
						.setParameter("id", id)
						.getResultList();
	}

	@Override
	public List<Reject> listByReceiver(final Long id) {
		return manager.createQuery(
				"FROM Reject AS reject " +
						"WHERE reject.receiver.id = :id",
				Reject.class)
						.setParameter("id", id)
						.getResultList();
	}

	@Override
	public void delete(Long id) {
		findById(id).ifPresent(reject -> {
			manager.remove(reject);
			manager.flush();
		});
	}

}
