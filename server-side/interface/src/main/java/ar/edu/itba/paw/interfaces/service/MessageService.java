package ar.edu.itba.paw.interfaces.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;

public interface MessageService {

	/**
	* <p>Envía un mensaje desde un usuario hacia la sala global de un bar o pub en
	* particular.</p>
	*
	* @param pubId
	*	El ID del pub/bar.
	* @param userId
	*	El ID del usuario que envía el mensaje.
	* @param message
	*	El mensaje enviado.
	*
	* @return
	*	Devuelve el resultado de procesar el mensaje, se haya enviado o no
	*	correctamente.
	*/
	public PubMessage sendToPub(final Long pubId, final Long userId, final Message message, final LocalDate date);

	/**
	* <p>Envía un mensaje desde un usuario hacia otro.</p>
	*
	* @param recipientId
	*	El ID del usuario receptor del mensaje.
	* @param senderId
	*	El ID del usuario que envía el mensaje.
	* @param message
	*	El mensaje enviado.
	*
	* @return
	*	Devuelve el resultado de procesar el mensaje, se haya enviado o no
	*	correctamente.
	*/
	public UserMessage sendToUser(final Long recipientId, final Long senderId, final Message message);

	/**
	* <p>Recolecta el último mensaje asociado al bar/pub en cuestión, opcionalmente
	* especificando el ID a partir del cual se debería buscar dicho mensaje.
	* En este último caso, se devuelven todos los mensajes hallados.</p>
	*
	* @param pubId
	*	El ID del pub/bar.
	* @param fromId
	*	El ID del último mensaje devuelto, a partir del cual buscar los siguientes.
	*
	* @return
	*	Devuelve el o los mensajes disponibles.
	*/
	public List<PubMessage> receiveFromPub(final Long pubId, final Optional<Long> fromId, final LocalDate date);

	/**
	* <p>Recolecta los últimos mensaje asociados a la sala privada entre dos usuarios
	* específicos.</p>
	*
	* @param recipientId
	*	El ID del usuario destino.
	* @param senderId
	*	El ID del usuario que desea recolectar los mensajes.
	* @param fromId
	*	El ID del último mensaje devuelto, a partir del cual buscar los siguientes.
	*
	* @return
	*	Devuelve el o los mensajes disponibles.
	*/
	public List<UserMessage> receiveFromUser(final Long recipientId, final Long senderId,
			final Optional<Long> fromId);
}
