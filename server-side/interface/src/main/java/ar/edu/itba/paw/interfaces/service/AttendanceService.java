package ar.edu.itba.paw.interfaces.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.relation.Attendance;

public interface AttendanceService {

	public Attendance create(final Long pubId, final Long userId, final LocalDate date);
	
	public Optional<Attendance> findById(final Long id);
	
	public Optional<Attendance> findByPubAndUserAndDate(final Long pubId, final Long userId, final LocalDate date);
	
	public Long getPubAttendanceCount(final Long pubId, final LocalDate date);
		
	public List<Attendance> getAttendanceByUser(final Long id);
	
	public List<User> getUsersByPubAndDate(final Long pubId, final LocalDate date);
	
	public void delete(final Long id);
		
}
