package ar.edu.itba.paw.webapp.dto.model.relation;

import java.util.List;

import ar.edu.itba.paw.webapp.dto.model.profile.ClientDto;

public class PendingsDto {
	
	private AttendanceDto attendance;
	private List<ClientDto> clients;
	
	public PendingsDto() {}
	
	public PendingsDto attendance(final AttendanceDto attendance) {
		this.attendance = attendance;
		return this;
	}
	
	public PendingsDto clients(final List<ClientDto> clients) {
		this.clients = clients;
		return this;
	}

	public AttendanceDto getAttendance() {
		return attendance;
	}

	public void setAttendance(final AttendanceDto attendance) {
		this.attendance = attendance;
	}

	public List<ClientDto> getClients() {
		return clients;
	}

	public void setClients(final List<ClientDto> clients) {
		this.clients = clients;
	}

}
