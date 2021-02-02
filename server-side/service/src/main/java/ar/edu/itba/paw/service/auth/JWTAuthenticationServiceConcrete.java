package ar.edu.itba.paw.service.auth;

import ar.edu.itba.paw.interfaces.auth.JWTAuthenticationService;
import ar.edu.itba.paw.interfaces.config.SecurityProperties;
import ar.edu.itba.paw.model.profile.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import javax.inject.Inject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JWTAuthenticationServiceConcrete implements JWTAuthenticationService {

	protected static final String JWT_ISSUER = "GetOut© (pawserver.it.itba.edu.ar/paw-2017b-3)";
	protected static final String JWT_AUDIENCE = "Release v3.0, REST API v1.0";
	protected static final String JWT_UID_CLAIM = "uid";

	protected final UserDetailsService userDetailsService;
	protected final SecurityProperties config;
	protected final Algorithm algorithm;
	protected final JWTVerifier verifier;

	@Inject
	public JWTAuthenticationServiceConcrete(
			final UserDetailsService userDetailsService,
			final SecurityProperties config) {
		this.userDetailsService = userDetailsService;
		this.config = config;
		this.algorithm = Algorithm.HMAC512(config.getJWTSecret());
		this.verifier = JWT.require(algorithm)
				.withIssuer(JWT_ISSUER)
				.withAudience(JWT_AUDIENCE)
				.build();
	}

	@Override
	public String createToken(final User user) {
		final Date now = new Date();
		return JWT.create()
			.withIssuer(JWT_ISSUER)
			.withAudience(JWT_AUDIENCE)
			.withIssuedAt(now)
			.withExpiresAt(new Date(now.getTime() + config.getJWTExpirationTimeInMilliseconds()))
			.withSubject(user.getUsername())
			.withClaim(JWT_UID_CLAIM, user.getId())
			.sign(algorithm);
	}

	@Override
	public Authentication getAuthentication(final String token) {
		final DecodedJWT jwt = verifier.verify(token);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}
