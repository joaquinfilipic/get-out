package ar.edu.itba.paw.interfaces.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface SecurityProperties {

	public PasswordEncoder getPasswordEncoder();
	public String getRememberKey();
	public int getTokenValidityInSeconds();
	public byte [] getJWTSecret();
	public long getJWTExpirationTimeInMilliseconds();
}
