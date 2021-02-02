package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.interfaces.auth.JWTAuthenticationService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.support.HTTPSupport;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {

	protected final JWTAuthenticationService jwtService;

	public JWTAuthenticationFilter(final JWTAuthenticationService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public void doFilter(
			final ServletRequest request,
			final ServletResponse response,
			final FilterChain chain)
					throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			final String token = HTTPSupport.getBearerToken(httpRequest);
			final SecurityContext context = SecurityContextHolder.getContext();
			if (!token.isEmpty()) {
				context.setAuthentication(jwtService.getAuthentication(token));
			}
			else {
				context.setAuthentication(null);
			}
			chain.doFilter(request, response);
		}
		catch (final GetOutException exception) {
			HTTPSupport.sendException(exception, httpResponse);
		}
		catch (final TokenExpiredException exception) {
			HTTPSupport.sendException(ErrorType.AUTHENTICATION_TOKEN_EXPIRED, httpResponse);
		}
		catch (final AuthenticationException exception) {
			HTTPSupport.sendException(ErrorType.FORBIDDEN_TOKEN, httpResponse);
		}
		catch (final JWTVerificationException exception) {
			/*
			 * AlgorithmMismatchException
			 * SignatureVerificationException
			 * InvalidClaimException
			 * JWTDecodeException
			 */
			HTTPSupport.sendException(ErrorType.INVALID_AUTHENTICATION_TOKEN, httpResponse);
		}
	}
}
