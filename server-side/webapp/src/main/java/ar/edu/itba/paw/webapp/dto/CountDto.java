package ar.edu.itba.paw.webapp.dto;

public class CountDto {
	
	private Long count;
	
	public CountDto() {}
	
	public CountDto count(final Long count) {
		this.count = count;
		return this;
	}
	
	public Long getCount() {
		return count;
	}
	
	public void setCount(final Long count) {
		this.count = count;
	}

}
