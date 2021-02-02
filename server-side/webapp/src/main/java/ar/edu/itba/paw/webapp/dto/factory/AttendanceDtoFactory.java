package ar.edu.itba.paw.webapp.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.webapp.dto.model.PubDto;
import ar.edu.itba.paw.webapp.dto.model.profile.UserDto;
import ar.edu.itba.paw.webapp.dto.model.relation.AttendanceDto;

public class AttendanceDtoFactory {
	
	public static AttendanceDto buildFromAttendance(final Attendance attendance) {
		return new AttendanceDto()
				.id(attendance.getId())
				.date(attendance.getDate())
				.pub(new PubDto()
						.id(attendance.getPub().getId())
						.name(attendance.getPub().getName())
						.openTime(attendance.getPub().getOpenTime().toString()))
				.user(new UserDto()
						.id(attendance.getUser().getId())
						.username(attendance.getUser().getUsername()));
	}
	
	public static List<AttendanceDto> buildFromAttendanceList(final List<Attendance> attendanceList) {
		return attendanceList.stream()
				.map(attendance -> buildFromAttendance(attendance))
				.collect(Collectors.toList());
	}

}
