package ar.edu.itba.paw.webapp.dto;

public class IdDto {
	
	private long id;
	
	public IdDto() {}
	
	public IdDto id(final long id) {
		this.id = id;
		return this;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(final long id) {
		this.id = id;
	}

}
