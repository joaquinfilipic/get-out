package ar.edu.itba.paw.webapp.rest.error;

import ar.edu.itba.paw.model.error.GetOutError;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class GetOutErrorController implements ExceptionMapper<GetOutException> {

	@Override
	public Response toResponse(final GetOutException exception) {
		return Response.status(exception.getStatus())
				.entity(new GetOutError(exception))
				.build();
	}
}
