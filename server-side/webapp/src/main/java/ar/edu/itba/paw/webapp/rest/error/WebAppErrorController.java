package ar.edu.itba.paw.webapp.rest.error;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.error.GetOutError;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ParamException.PathParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

	/**
	* <p>Controla los errores asociados a la infraestructura web. Los errores
	* desconocidos deben mapearse hacia errores internos del servidor para
	* evitar que el cliente identifique posibles <i>exploits</i>.</p>
	*/

@Provider
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class WebAppErrorController implements ExceptionMapper<WebApplicationException> {

	private static final Logger log = LoggerFactory.getLogger(WebAppErrorController.class);

	@Override
	public Response toResponse(final WebApplicationException exception) {
		final GetOutException getoutException = inferException(exception);
		return Response.status(getoutException.getStatus())
				.header(HttpHeaders.CONTENT_TYPE, GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
				.entity(new GetOutError(getoutException))
				.build();
	}

	protected GetOutException inferException(final WebApplicationException exception) {
		log.error("----------------------------------------------------------------------------------------------------");
		log.error(format("WEBAPP ERROR FOUND -> Exception Class: %s\tMessage: %s.",
				exception.getClass().getCanonicalName(), exception.getMessage()));
		if (exception instanceof NotFoundException) {
			return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
		}
		else if (exception instanceof NotAllowedException) {
			return GetOutException.of(ErrorType.METHOD_NOT_ALLOWED);
		}
		else if (exception instanceof NotSupportedException) {
			return GetOutException.of(ErrorType.UNSUPPORTED_MEDIA_TYPE);
		}
		else if (exception instanceof PathParamException) {
			return GetOutException.of(ErrorType.INVALID_PATH_PARAMETER);
		}
		else {
			return GetOutException.of(ErrorType.UNKNOWN_ERROR);
		}
	}
}
