package ar.edu.itba.paw.webapp.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartException;

public class MultipartExceptionHandler extends OncePerRequestFilter {

	private static final Logger logger
		= LoggerFactory.getLogger(MultipartExceptionHandler.class);

	@Override
	protected void doFilterInternal(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain chain)
					throws ServletException, IOException {
		try {
			chain.doFilter(request, response);
		}
		catch (final MultipartException exception) {
			final Optional<Throwable> rootCause = Optional.ofNullable(exception.getRootCause());
			logger.info("Se intentó subir un archivo que superó el límite establecido.");
			rootCause.ifPresent(cause -> logger.info("Causa: '{}'", cause.getMessage()));
			response.resetBuffer();
			response.sendRedirect(request.getContextPath() + "/profile?tooBig");
		}
	}
}
