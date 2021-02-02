package ar.edu.itba.paw.interfaces.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.relation.Attendance;;

public interface AttendanceDAO {

	public Attendance create(final Long pubId, final Long userId, final LocalDate date);

	public Optional<Attendance> findById(final Long id);

	public Optional<Attendance> findByPubAndUserAndDate(final Long pubId, final Long userId, final LocalDate date);

	public List<Attendance> listAttendanceByPubAndDate(final Long pubId, final LocalDate date);
	
	public List<Attendance> listAttendanceByUser(final Long userId);
	
	public List<User> listUsersByPubAndDate(final Long pubId, final LocalDate date);

	public Long getPubAttendanceCount(final Long pubId, final LocalDate date);

	public void deleteById(final Long id);

}
