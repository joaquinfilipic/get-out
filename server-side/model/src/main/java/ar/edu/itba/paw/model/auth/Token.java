package ar.edu.itba.paw.model.auth;

public class Token {

	final String token;

	public Token(final String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
