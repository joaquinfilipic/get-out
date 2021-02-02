package ar.edu.itba.paw.webapp.dto.model.profile;

public class TokenDto {
	
	private Long id;
	private String token;
	
	public TokenDto() {}
	
	public TokenDto id(final Long id) {
		this.id = id;
		return this;
	}
	
	public TokenDto token(final String token) {
		this.token = token;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

}
