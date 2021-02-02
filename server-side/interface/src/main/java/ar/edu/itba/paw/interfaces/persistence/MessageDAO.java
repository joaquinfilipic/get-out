package ar.edu.itba.paw.interfaces.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;

public interface MessageDAO {

	public PubMessage sendToPub(final Long pubId, final Long userId, final Message message, final LocalDate date);
	public UserMessage sendToUser(final Long recipientId, final Long senderId, final Message message);

	public List<PubMessage> receiveFromPub(final Long pubId, final Optional<Long> fromId, final LocalDate date);
	public List<UserMessage> receiveFromUser(final Long recipientId, final Long senderId, final Optional<Long> fromId);
}
