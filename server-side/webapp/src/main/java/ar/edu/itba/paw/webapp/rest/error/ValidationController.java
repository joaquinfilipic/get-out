package ar.edu.itba.paw.webapp.rest.error;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.error.ValidationError;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

	/**
	* <p>Controlador encargado de traducir las excepciones producidas durante
	* una validación fallida. Además de generar el error, incluye información
	* específica sobre las validaciones afectadas.</p>
	*/

@Provider
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class ValidationController implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(final ConstraintViolationException exception) {
		return Response.status(ErrorType.CONSTRAINT_VIOLATION.getStatus())
				.entity(new ValidationError(exception))
				.build();
	}
}
