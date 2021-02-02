package ar.edu.itba.paw.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.relation.LikeDAO;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.relation.Likes;

@Repository
public class LikesJPA implements LikeDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Likes create(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		final Likes like = new Likes.Builder()
				.sender(manager.find(User.class, senderId))
				.receiver(manager.find(User.class, receiverId))
				.pub(manager.find(Pub.class, pubId))
				.date(date)
				.build();
		manager.persist(like);
		manager.flush();
		return like;
	}
	
	@Override
	public Optional<Likes> findById(final Long id) {
		return Optional.ofNullable(manager.find(Likes.class, id));
	}
	
	public Optional<Likes> findBySenderAndReceiverAndPubAndDate(final Long senderId, final Long receiverId, 
			final Long pubId, final LocalDate date) {
		try {
			return Optional.of(manager.createQuery(
					"FROM Likes AS likes " +
							"WHERE likes.sender.id = :senderId " +
							"AND likes.receiver.id = :receiverId " +
							"AND likes.pub.id = :pubId " +
							"AND likes.date = :date",
					Likes.class)
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
	public List<Likes> listBySenderAndPubAndDate(final Long senderId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Likes AS likes " +
						"WHERE likes.sender.id = :senderId " +
						"AND likes.pub.id = :pubId " +
						"AND likes.date = :date",
				Likes.class)
						.setParameter("senderId", senderId)
						.setParameter("pubId", pubId)
						.setParameter("date", date)
						.getResultList();
	}

	@Override
	public List<Likes> listByReceiverAndPubAndDate(final Long receiverId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Likes AS likes " +
						"WHERE likes.receiver.id = :receiverId " +
						"AND likes.pub.id = :pubId " +
						"AND likes.date = :date",
				Likes.class)
						.setParameter("receiverId", receiverId)
						.setParameter("pubId", pubId)
						.setParameter("date", date)
						.getResultList();
	}

	@Override
	public List<Likes> listBySender(final Long id) {
		return manager.createQuery(
				"FROM Likes AS likes " +
						"WHERE likes.sender.id = :id",
				Likes.class)
						.setParameter("id", id)
						.getResultList();
	}

	@Override
	public List<Likes> listByReceiver(final Long id) {
		return manager.createQuery(
				"FROM Likes AS likes " +
						"WHERE likes.receiver.id = :id",
				Likes.class)
						.setParameter("id", id)
						.getResultList();
	}

	@Override
	public void delete(final Long id) {
		findById(id).ifPresent(like -> {
			manager.remove(like);
			manager.flush();
		});
	}

}
