package ar.edu.itba.paw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.relation.AttendanceDAO;
import ar.edu.itba.paw.interfaces.service.AttendanceService;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.relation.Attendance;

@Service
@Transactional
public class AttendanceServiceConcrete implements AttendanceService {

	@Autowired
	AttendanceDAO attendanceDAO;

	@Override
	public Attendance create(final Long pubId, final Long userId, final LocalDate date) {
		return attendanceDAO.create(pubId, userId, date);
	}
	
	@Override
	public Optional<Attendance> findById(final Long id) {
		return attendanceDAO.findById(id);
	}

	@Override
	public Optional<Attendance> findByPubAndUserAndDate(final Long pubId, final Long userId, final LocalDate date) {
		return attendanceDAO.findByPubAndUserAndDate(pubId, userId, date);
	}

	@Override
	public Long getPubAttendanceCount(final Long pubId, final LocalDate date) {
		return attendanceDAO.getPubAttendanceCount(pubId, date);
	}
	
	@Override
	public List<Attendance> getAttendanceByUser(final Long userId) {
		return attendanceDAO.listAttendanceByUser(userId);
	}

	@Override
	public List<User> getUsersByPubAndDate(final Long pubId, final LocalDate date) {
		return attendanceDAO.listUsersByPubAndDate(pubId, date);
	}

	@Override
	public void delete(final Long id) {
		attendanceDAO.deleteById(id);
	}

}
