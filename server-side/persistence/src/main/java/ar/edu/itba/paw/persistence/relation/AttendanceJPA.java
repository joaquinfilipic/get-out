package ar.edu.itba.paw.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.persistence.relation.AttendanceDAO;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.relation.Attendance;

@Repository
public class AttendanceJPA implements AttendanceDAO {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Attendance create(final Long pubId, final Long userId, final LocalDate date) {
		final Attendance attendance = new Attendance.Builder()
				.pub(manager.find(Pub.class, pubId))
				.user(manager.find(User.class, userId))
				.date(date)
				.build();
		manager.persist(attendance);
		manager.flush();
		return attendance;
	}
	
	@Override
	public Optional<Attendance> findById(final Long id) {
		return Optional.ofNullable(manager.find(Attendance.class, id));
	}
	
	@Override
	public Optional<Attendance> findByPubAndUserAndDate(final Long pubId, final Long userId, final LocalDate date) {
		try {
			return Optional.of(manager.createQuery(
					"FROM Attendance AS attendance " +
					"WHERE attendance.pub.id = :pubId " +
							"AND attendance.user.id = :userId " +
							"AND attendance.date = :date",
					Attendance.class)
							.setParameter("pubId", pubId)
							.setParameter("userId", userId)
							.setParameter("date", date)
							.getSingleResult());
		} catch (NoResultException exception) {
			return Optional.empty();
		}
	}
	
	@Override
	public List<Attendance> listAttendanceByPubAndDate(final Long pubId,
			final LocalDate date) {
		return manager.createQuery(
				"FROM Attendance AS attendance " +
				"WHERE attendance.pub.id = :id " +
						"AND attendance.date = :date",
				Attendance.class)
						.setParameter("id", pubId)
						.setParameter("date", date)
						.getResultList();
	}
	
	@Override
	public List<Attendance> listAttendanceByUser(final Long userId) {
		return manager.createQuery(
				"FROM Attendance AS attendance " 					+
				"WHERE attendance.user.id = :id " 	 				+
					"AND attendance.date >= :nowDate " 				+
				"ORDER BY attendance.date, attendance.pub.openTime", 
				Attendance.class)
						.setParameter("id", userId)
						.setParameter("nowDate", LocalDate.now())
						.getResultList();
	}
	
	@Override
	public List<User> listUsersByPubAndDate(Long pubId, LocalDate date) {
		return manager.createQuery(
				"SELECT user FROM Attendance AS attendance " +
				"WHERE attendance.pub.id = :id " +
						"AND attendance.date = :date",
				User.class)
						.setParameter("id", pubId)
						.setParameter("date", date)
						.getResultList();
	}
	
	@Override
	public Long getPubAttendanceCount(final Long pubId, final LocalDate date) {
		return manager.createQuery(
				"SELECT COUNT(attendance) FROM Attendance AS attendance " +
				"WHERE attendance.pub.id = :id " +
						"AND attendance.date = :date",
				Long.class)
						.setParameter("id", pubId)
						.setParameter("date", date)
						.getSingleResult();
	}
	
	@Override
	public void deleteById(final Long id) {
		findById(id).ifPresent(attendance -> {
			manager.remove(attendance);
			manager.flush();
		});
	}

}
