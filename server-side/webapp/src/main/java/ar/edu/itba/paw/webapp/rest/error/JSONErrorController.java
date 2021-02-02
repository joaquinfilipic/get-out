package ar.edu.itba.paw.webapp.rest.error;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.error.JSONError;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

	/**
	* <p>Este controlador se encarga de resolver las excepciones asociadas a la
	* serialización y deserialización de entidades en formato JSON.</p>
	*/

@Provider
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class JSONErrorController implements ExceptionMapper<JsonProcessingException> {

	@Override
	public Response toResponse(final JsonProcessingException exception) {
		return Response.status(ErrorType.JSON_ENTITY_MALFORMED.getStatus())
				.entity(new JSONError(exception))
				.build();
	}
}
