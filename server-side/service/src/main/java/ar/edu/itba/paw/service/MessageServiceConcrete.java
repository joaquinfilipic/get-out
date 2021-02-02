package ar.edu.itba.paw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.MessageDAO;
import ar.edu.itba.paw.interfaces.service.MessageService;
import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;

@Service
@Transactional
public class MessageServiceConcrete implements MessageService {

	protected final MessageDAO messageDAO;

	@Autowired
	public MessageServiceConcrete(final MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	@Override
	public PubMessage sendToPub(final Long pubId, final Long userId, final Message message, final LocalDate date) {
		return messageDAO.sendToPub(pubId, userId, message, date);
	}

	@Override
	public UserMessage sendToUser(final Long recipientId, final Long senderId, final Message message) {
		return messageDAO.sendToUser(recipientId, senderId, message);
	}

	@Override
	public List<PubMessage> receiveFromPub(final Long pubId, final Optional<Long> fromId, final LocalDate date) {
		return messageDAO.receiveFromPub(pubId, fromId, date);
	}

	@Override
	public List<UserMessage> receiveFromUser(final Long recipientId, final Long senderId, final Optional<Long> fromId) {
		return messageDAO.receiveFromUser(recipientId, senderId, fromId);
	}
}
