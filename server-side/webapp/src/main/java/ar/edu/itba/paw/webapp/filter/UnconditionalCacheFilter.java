package ar.edu.itba.paw.webapp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

	/**
	* <p>Implementa un sistema de caché incondicional, que permite emitir
	* recursos estáticos, con un período de renovación de 10 años o, en caso
	* de que el explorador lo soporte, bajo inmutabilidad (para siempre).</p>
	*/

public class UnconditionalCacheFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain chain)
					throws ServletException, IOException {
		response.setHeader("Cache-Control", "public, max-age=315360000, immutable");
		chain.doFilter(request, response);
	}
}
