package ar.edu.itba.paw.webapp.rest.error;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.error.GetOutError;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

	/**
	* <p>Se encarga de atrapar todas las excepciones que no fueron atrapadas en
	* controladores más específicos, y de convertirlas en mensajes de error más
	* uniformes.</p>
	*/

@Provider
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class FallbackErrorController implements ExceptionMapper<Exception> {

	private static final Logger logger
		= LoggerFactory.getLogger(FallbackErrorController.class);

	@Override
	public Response toResponse(final Exception exception) {
		final GetOutException getoutException = inferException(exception);
		return Response.status(getoutException.getStatus())
				.header(HttpHeaders.CONTENT_TYPE, GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
				.entity(new GetOutError(getoutException))
				.build();
	}

	protected GetOutException inferException(final Exception exception) {
		if (exception instanceof BadCredentialsException) {
			return GetOutException.of(ErrorType.BAD_CREDENTIALS);
		}
		else {
			exception.printStackTrace();
			logger.error("Unknown Exception (class = {}): '{}'. Caused by '{}'.",
					exception.getClass().getCanonicalName(),
					exception.getMessage(),
					exception.getCause());
			return GetOutException.of(ErrorType.UNKNOWN_ERROR);
		}
	}
}
