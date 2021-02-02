
	package ar.edu.itba.paw.persistence.realtime;

	import static org.hamcrest.Matchers.*;
	import static org.junit.Assert.*;

	import java.time.LocalDate;
	import java.time.LocalDateTime;
	import java.util.List;
	import java.util.Optional;

	import org.junit.Before;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.test.context.ContextConfiguration;
	import org.springframework.test.context.jdbc.Sql;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.MessageDAO;
import ar.edu.itba.paw.model.chat.Message;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;
import ar.edu.itba.paw.model.profile.User;
	import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.persistence.support.PersistenceTestConfig;
	import ar.edu.itba.paw.persistence.support.TestDataFactory;

	@Transactional
	@Sql("classpath:test.sql")
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(classes = PersistenceTestConfig.class)
	public class MessageJPATest {

		private static final LocalDate DATE = LocalDate.parse("2017-11-01");

		private User [] users;
		private Pub [] pubs;

		@Autowired
		private TestDataFactory factory;

		@Autowired
		private MessageDAO dao;

		@Before
		public void setUp() throws Exception {

			factory.clean("PubMessage");
			factory.clean("UserMessage");
			factory.cleanUsers();
			factory.cleanPubs();

			users = factory.createUserArray("MessageJPATest", 10);
			pubs = factory.createPubArray("MessageJPATest", users);
		}

		/**
		* <p>Debería poder enviar un mensaje a un pub determinado, y los
		* parámetros enviados deberían retornar sin modificación.</p>
		*/

		@Test
		public void canSendAMessageToPub() {

			final Message message = new Message("Hola que tal! Cómo va este test?");

			final PubMessage pubMessage = dao.sendToPub(
					pubs[0].getId(), users[0].getId(), message, DATE);

			assertThat(pubMessage.getMessage(), is(equalTo(message.getMessage())));
			assertThat(pubMessage.getPub().getId(), is(equalTo(pubs[0].getId())));
			assertThat(pubMessage.getUser().getId(), is(equalTo(users[0].getId())));
			assertThat(pubMessage.getDate(), is(equalTo(DATE)));
		}

		/**
		* <p>Debería poder enviar un mensaje a un usuario determinado, y los
		* parámetros enviados deberían retornar sin modificación.</p>
		*/

		@Test
		public void canSendAMessageToUser() {

			final Message message = new Message("Hola que tal! Cómo va este test?");

			final UserMessage userMessage = dao.sendToUser(
					users[1].getId(), users[0].getId(), message);

			assertThat(userMessage.getMessage(), is(equalTo(message.getMessage())));
			assertThat(userMessage.getRecipient().getId(), is(equalTo(users[1].getId())));
			assertThat(userMessage.getSender().getId(), is(equalTo(users[0].getId())));
		}

		/**
		* <p>Con respecto al tiempo absoluto, un mensaje enviado debería
		* poseer un <i>timestamp</i> mayor o igual al actual.</p>
		*/

		@Test
		public void pubMessageTimestampShouldBeIncreasing() {

			final LocalDateTime base = LocalDateTime.now();
			final Message message = new Message("Test de Timestamp!!!");

			final PubMessage pubMessage = dao.sendToPub(
					pubs[0].getId(), users[0].getId(), message, DATE);

			assertThat(pubMessage.getTimestamp(), is(greaterThanOrEqualTo(base)));
		}

		@Test
		public void userMessageTimestampShouldBeIncreasing() {

			final LocalDateTime base = LocalDateTime.now();
			final Message message = new Message("Test de Timestamp!!!");

			final UserMessage userMessage = dao.sendToUser(
					users[1].getId(), users[0].getId(), message);

			assertThat(userMessage.getTimestamp(), is(greaterThanOrEqualTo(base)));
		}

		/**
		* <p>Enviar dos mensajes implica que el <i>timestamp</i> del segundo
		* es mayor o igual al primero, pero nunca al revés.</p>
		*/

		@Test
		public void pubMessageRelativeTimestampShouldBeIncreasing() {

			final Message firstMessage = new Message("Este es el primer mensaje.");
			final Message secondMessage = new Message("Este es el segundo mensaje.");

			final PubMessage firstPubMessage = dao.sendToPub(
					pubs[0].getId(), users[0].getId(), firstMessage, DATE);

			TestDataFactory.sleep(50);

			final PubMessage secondPubMessage = dao.sendToPub(
					pubs[0].getId(), users[0].getId(), secondMessage, DATE);

			assertThat(secondPubMessage.getTimestamp(),
					is(greaterThanOrEqualTo(firstPubMessage.getTimestamp())));
		}

		@Test
		public void userMessageRelativeTimestampShouldBeIncreasing() {

			final Message firstMessage = new Message("Este es el primer mensaje.");
			final Message secondMessage = new Message("Este es el segundo mensaje.");

			final UserMessage firstUserMessage = dao.sendToUser(
					users[1].getId(), users[0].getId(), firstMessage);

			TestDataFactory.sleep(50);

			final UserMessage secondUserMessage = dao.sendToUser(
					users[1].getId(), users[0].getId(), secondMessage);

			assertThat(secondUserMessage.getTimestamp(),
					is(greaterThanOrEqualTo(firstUserMessage.getTimestamp())));
		}

		/**
		* <p>Si se envían dos mensajes a distinto pubo usuario, la
		* recuperación de los mensajes de uno de ellos no debería contener
		* ambos mensajes.</p>
		*/

		@Test
		public void messagesNotCollideBetweenPubs() {

			final Message firstMessage = new Message("Mensaje para un pub.");
			final Message secondMessage = new Message("Mensaje para otro pub.");

			dao.sendToPub(pubs[0].getId(), users[0].getId(), firstMessage, DATE);
			dao.sendToPub(pubs[1].getId(), users[0].getId(), secondMessage, DATE);

			final List<PubMessage> firstMessages
				= dao.receiveFromPub(pubs[0].getId(), Optional.empty(), DATE);
			final List<PubMessage> secondMessages
				= dao.receiveFromPub(pubs[1].getId(), Optional.empty(), DATE);

			assertThat("Hubo una colisión en el primer Pub",
					firstMessages, hasSize(1));
			assertThat("Hubo una colisión en el segundo Pub",
					secondMessages, hasSize(1));

			assertThat("Falla de integridad en el primer mensaje",
					firstMessages,
						contains(hasProperty("message",
								is(equalTo(firstMessage.getMessage())))));
			assertThat("Falla de integridad en el segundo mensaje",
					secondMessages,
						contains(hasProperty("message",
								is(equalTo(secondMessage.getMessage())))));
		}

		@Test
		public void messagesNotCollideBetweenUsers() {

			final Message firstMessage = new Message("Mensaje para un usuario.");
			final Message secondMessage = new Message("Mensaje para otro usuario.");

			dao.sendToUser(users[1].getId(), users[0].getId(), firstMessage);
			dao.sendToUser(users[2].getId(), users[0].getId(), secondMessage);

			final List<UserMessage> firstMessages
				= dao.receiveFromUser(users[1].getId(), users[0].getId(), Optional.empty());
			final List<UserMessage> secondMessages
				= dao.receiveFromUser(users[2].getId(), users[0].getId(), Optional.empty());

			assertThat("Hubo una colisión en el primer usuario",
					firstMessages, hasSize(1));
			assertThat("Hubo una colisión en el segundo usuario",
					secondMessages, hasSize(1));

			assertThat("Falla de integridad en el primer mensaje",
					firstMessages,
						contains(hasProperty("message",
								is(equalTo(firstMessage.getMessage())))));
			assertThat("Falla de integridad en el segundo mensaje",
					secondMessages,
						contains(hasProperty("message",
								is(equalTo(secondMessage.getMessage())))));
		}

		/**
		* <p>En caso de no especificar un <i>timestamp</i> en particular,
		* debería poder obtener todos los mensajes de un pub/usuario, con un
		* único request.</p>
		*/

		@Test
		public void canRetrieveAllPubMessagesAtOnce() {

			// Enviaremos un mensaje por usuario:
			final int MESSAGES = users.length;

			for (int i = 0; i < MESSAGES; ++i)
				dao.sendToPub(pubs[0].getId(), users[i].getId(),
						new Message("Mensaje número " + i), DATE);

			final List<PubMessage> messages
				= dao.receiveFromPub(pubs[0].getId(), Optional.empty(), DATE);

			assertThat("Algunos mensajes no se pudieron recuperar",
					messages, hasSize(MESSAGES));
		}

		@Test
		public void canRetrieveAllUserMessagesAtOnce() {

			// Enviaremos 10 mensajes:
			final int MESSAGES = 10;

			for (int i = 0; i < MESSAGES; ++i)
				dao.sendToUser(users[1].getId(), users[0].getId(),
						new Message("Mensaje número " + i));

			final List<UserMessage> messages
				= dao.receiveFromUser(users[1].getId(), users[0].getId(),
						Optional.empty());

			assertThat("Algunos mensajes no se pudieron recuperar",
					messages, hasSize(MESSAGES));
		}

		/**
		* <p>Debería poder recuperar mensajes desde cierto <i>timestamp</i>,
		* lo que impide descargar la casilla de mensajes completa en cada
		* request.</p>
		*/

		@Test
		public void canRetrievePubMessagesFromSpecificId() {

			// Enviaremos 10 mensajes de prueba:
			final int MESSAGES = 10;
			// Recuperaremos la mitad:
			final int RETRIEVED = 5;
			
			Long halfId = 0L;

			for (int i = 0; i < MESSAGES; ++i) {
				
				final PubMessage message = dao.sendToPub(
						pubs[0].getId(), users[0].getId(),
						new Message("Mensaje número " + i), DATE);
				
				if (i == (MESSAGES - RETRIEVED - 1))
					halfId = message.getId();
				
			}

			final List<PubMessage> messages
				= dao.receiveFromPub(pubs[0].getId(), Optional.of(halfId), DATE);

			assertThat("No se recuperaron los mensajes especificados",
					messages, hasSize(RETRIEVED));
		}

		@Test
		public void canRetrieveUserMessagesFromSpecificId() {

			// Enviaremos 10 mensajes de prueba:
			final int MESSAGES = 10;
			// Recuperaremos la mitad:
			final int RETRIEVED = 5;
			
			Long halfId = 0L;

			for (int i = 0; i < MESSAGES; ++i) {

				final UserMessage message = dao.sendToUser(
						users[1].getId(), users[0].getId(),
						new Message("Mensaje número " + i));
				
				if (i == (MESSAGES - RETRIEVED - 1))
					halfId = message.getId();
				
			}

			final List<UserMessage> messages 
				= dao.receiveFromUser(users[1].getId(), users[0].getId(), Optional.of(halfId));

			assertThat("No se recuperaron los mensajes especificados",
					messages, hasSize(RETRIEVED));
		}
	}
