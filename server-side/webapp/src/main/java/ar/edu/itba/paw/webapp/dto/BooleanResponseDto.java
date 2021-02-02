package ar.edu.itba.paw.webapp.dto;

public class BooleanResponseDto {
	
	private boolean response;
	
	public BooleanResponseDto() {}
	
	public BooleanResponseDto response(final boolean response) {
		this.response = response;
		return this;
	}
	
	public boolean getResponse() {
		return response;
	}
	
	public void setResponse(final boolean response) {
		this.response = response;
	}

}
