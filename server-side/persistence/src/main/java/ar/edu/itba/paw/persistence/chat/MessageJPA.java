package ar.edu.itba.paw.persistence.chat;

import ar.edu.itba.paw.interfaces.persistence.MessageDAO;
import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MessageJPA implements MessageDAO {

	@PersistenceContext
	protected EntityManager manager;

	@Override
	public PubMessage sendToPub(final Long pubId, final Long userId,
			final Message message, final LocalDate date) {
		final PubMessage pubMessage = new PubMessage.Builder()
				.user(manager.find(User.class, userId))
				.pub(manager.find(Pub.class, pubId))
				.timestamp(LocalDateTime.now())
				.message(message.getMessage())
				.date(date)
				.build();
		manager.persist(pubMessage);
		manager.flush();
		return pubMessage;
	}

	@Override
	public UserMessage sendToUser(final Long recipientId, final Long senderId, final Message message) {
		final UserMessage userMessage = new UserMessage.Builder()
				.sender(manager.find(User.class, senderId))
				.recipient(manager.find(User.class, recipientId))
				.timestamp(LocalDateTime.now())
				.message(message.getMessage())
				.build();
		manager.persist(userMessage);
		manager.flush();
		return userMessage;
	}

	@Override
	public List<PubMessage> receiveFromPub(final Long pubId, final Optional<Long> fromId, final LocalDate date) {
		if (fromId.isPresent()) {
			return manager.createQuery("FROM PubMessage AS message "
					+ "WHERE message.pub.id = :pubId " + "AND message.date = :date "
					+ "AND message.id > :fromId "
					+ "ORDER BY message.timestamp", PubMessage.class)
					.setParameter("pubId", pubId)
					.setParameter("fromId", fromId.get())
					.setParameter("date", date)
					.getResultList();
		}
		else {
			return manager.createQuery("FROM PubMessage AS message "
					+ "WHERE message.pub.id = :pubId "
					+ "AND message.date = :date "
					+ "ORDER BY message.timestamp", PubMessage.class)
					.setParameter("pubId", pubId)
					.setParameter("date", date)
					.getResultList();
		}
	}

	@Override
	public List<UserMessage> receiveFromUser(final Long recipientId, final Long senderId, final Optional<Long> fromId) {
		if (fromId.isPresent()) {
			return manager.createQuery("FROM UserMessage AS message "
					+ "WHERE ((message.sender.id = :sender AND message.recipient.id = :recipient) "
					+ "OR (message.sender.id = :recipient AND message.recipient.id = :sender)) "
					+ "AND message.id > :fromId " 
					+ "ORDER BY message.timestamp", UserMessage.class)
					.setParameter("sender", senderId)
					.setParameter("recipient", recipientId)
					.setParameter("fromId", fromId.get())
					.getResultList();
		}
		else {
			return manager.createQuery("FROM UserMessage AS message "
					+ "WHERE (message.sender.id = :sender AND message.recipient.id = :recipient) "
					+ "OR (message.sender.id = :recipient AND message.recipient.id = :sender) "
					+ "ORDER BY message.timestamp", UserMessage.class)
					.setParameter("sender", senderId)
					.setParameter("recipient", recipientId)
					.getResultList();
		}
	}
}
