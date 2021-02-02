package ar.edu.itba.paw.persistence.profile;

import ar.edu.itba.paw.interfaces.persistence.ClientDAO;
import ar.edu.itba.paw.model.profile.Client;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClientJPA implements ClientDAO {

	@PersistenceContext
	protected EntityManager manager;

	@Override
	public Optional<Client> findById(final Long id) {
		return Optional.ofNullable(manager.find(Client.class, id));
	}

	@Override
	public Client update(final Client client) {
		return manager.merge(client);
	}
	
	@Override
	public List<Client> getPendingByPubAndDate(final Long userId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Client AS client " 							+
				"WHERE client.id IN "    							+
				/*
				 * user in users who liked the caller
				 */
				"( "											  	+
					"SELECT likes.sender.id FROM Likes AS likes " 	+
					"WHERE likes.receiver.id = :userId " 			+
						"AND likes.pub.id = :pubId " 				+
						"AND likes.date = :date " 					+
				") " 												+
				"AND client.id NOT IN " 							+
				/*
				 * user not in users already liked by the caller
				 */
				"( "												+
					"SELECT likes.receiver.id FROM Likes AS likes " +
					"WHERE likes.sender.id = :userId " 				+
						"AND likes.pub.id = :pubId " 				+
						"AND likes.date = :date " 					+
				") "												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users who rejected the caller
				 */
				"( "												+
					"SELECT rej.sender.id FROM Reject AS rej "		+
					"WHERE rej.receiver.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") " 												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users rejected by the caller
				 */
				"( "												+
					"SELECT rej.receiver.id FROM Reject AS rej "	+
					"WHERE rej.sender.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") ", 
				Client.class)
					.setParameter("userId", userId)
					.setParameter("pubId", pubId)
					.setParameter("date", date)
					.getResultList();
	}

	@Override
	public List<Client> getCandidatesByPubAndDate(final Long userId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Client AS client " 							+
				"WHERE client.id IN " 								+
				/*
				 * user attends the event
				 */
				"( " 												+
					"SELECT att.user.id FROM Attendance AS att " 	+
					"WHERE att.pub.id = :pubId " 					+
						"AND att.date = :date " 					+
				") " 												+
				"AND client.id NOT IN "    						+
				/*
				 * user not in users already liked by the caller
				 */
				"( "												+
					"SELECT likes.receiver.id FROM Likes AS likes " +
					"WHERE likes.sender.id = :userId " 				+
						"AND likes.pub.id = :pubId " 				+
						"AND likes.date = :date " 					+
				") "												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users who rejected the caller
				 */
				"( "												+
					"SELECT rej.sender.id FROM Reject AS rej "		+
					"WHERE rej.receiver.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") " 												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users rejected by the caller
				 */
				"( "												+
					"SELECT rej.receiver.id FROM Reject AS rej "	+
					"WHERE rej.sender.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") "												+
				/*
				 * and finally, user is not self
				 */
				"AND client.id != :userId", 
				Client.class)
					.setParameter("userId", userId)
					.setParameter("pubId", pubId)
					.setParameter("date", date)
					.getResultList();
	}

	@Override
	public List<Client> getMatchesByPubAndDate(final Long userId, final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"FROM Client AS client " 							+
				"WHERE client.id IN "    							+
				/*
				 * user in users who liked the caller
				 */
				"( "											  	+
					"SELECT likes.sender.id FROM Likes AS likes " 	+
					"WHERE likes.receiver.id = :userId " 			+
						"AND likes.pub.id = :pubId " 				+
						"AND likes.date = :date " 					+
				") " 												+
				"AND client.id IN " 								+
				/*
				 * user in users already liked by the caller
				 */
				"( "												+
					"SELECT likes.receiver.id FROM Likes AS likes " +
					"WHERE likes.sender.id = :userId " 				+
						"AND likes.pub.id = :pubId " 				+
						"AND likes.date = :date " 					+
				") "												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users who rejected the caller
				 */
				"( "												+
					"SELECT rej.sender.id FROM Reject AS rej "		+
					"WHERE rej.receiver.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") " 												+
				"AND client.id NOT IN "								+
				/*
				 * user not in users rejected by the caller
				 */
				"( "												+
					"SELECT rej.receiver.id FROM Reject AS rej "	+
					"WHERE rej.sender.id = :userId "				+
						"AND rej.pub.id = :pubId "					+
						"AND rej.date = :date "						+
				") ", 
				Client.class)
					.setParameter("userId", userId)
					.setParameter("pubId", pubId)
					.setParameter("date", date)
					.getResultList();
	}
	
}
