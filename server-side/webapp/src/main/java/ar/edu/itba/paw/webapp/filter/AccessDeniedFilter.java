package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

	/**
	 * <p>Se encarga de atrapar excepciones de tipo <i>AccessDeniedException
	 * </i>producidas por <i>Spring</i>, y de traducirlas a excepciones
	 * customizadas de <i>GetOut©</i>.</p>
	 */

public class AccessDeniedFilter extends GenericFilterBean {

	@Override
	public void doFilter(
			final ServletRequest request,
			final ServletResponse response,
			final FilterChain chain)
					throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		}
		catch (final AccessDeniedException exception) {
			throw GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN);
		}
	}
}
