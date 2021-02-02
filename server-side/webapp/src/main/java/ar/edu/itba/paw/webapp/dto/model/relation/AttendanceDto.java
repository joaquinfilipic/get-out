package ar.edu.itba.paw.webapp.dto.model.relation;

import java.time.LocalDate;

import ar.edu.itba.paw.webapp.dto.model.PubDto;
import ar.edu.itba.paw.webapp.dto.model.profile.UserDto;

public class AttendanceDto {
	
	private Long id;
	private LocalDate date;
	private PubDto pub;
	private UserDto user;
	
	public AttendanceDto id(final Long id) {
		this.id = id;
		return this;
	}
	
	public AttendanceDto date(final LocalDate date) {
		this.date = date;
		return this;
	}
	
	public AttendanceDto pub(final PubDto pub) {
		this.pub = pub;
		return this;
	}
	
	public AttendanceDto user(final UserDto user) {
		this.user = user;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDate() {
		return date.toString();
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}
	
	public PubDto getPub() {
		return pub;
	}
	
	public void setPub(final PubDto pub) {
		this.pub = pub;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(final UserDto user) {
		this.user = user;
	}

}
