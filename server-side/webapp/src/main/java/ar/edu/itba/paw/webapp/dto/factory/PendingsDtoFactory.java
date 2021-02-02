package ar.edu.itba.paw.webapp.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.webapp.dto.model.relation.PendingsDto;

public class PendingsDtoFactory {
	
	public static PendingsDto buildFromAttendanceAndClients(final Attendance attendance, final List<Client> clients) {
		return new PendingsDto()
				.attendance(AttendanceDtoFactory.buildFromAttendance(attendance))
				.clients(clients.stream()
						.map(client -> {
							return ClientDtoFactory.buildReducedFromClient(client);
						}).collect(Collectors.toList()));
	}

}
