package ar.edu.itba.paw.webapp.rest.pubInformation;

import java.net.URI;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Controller;

import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.CoordinatesService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.information.Coordinates;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.form.pub.information.CoordinatesForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class CoordinatesController {
		
	private final CoordinatesService coordinatesService;
	private final PubService pubService;
	
	@Inject
	public CoordinatesController(final CoordinatesService coordinatesService, final PubService pubService) {
		this.coordinatesService = coordinatesService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/coordinates")
	public Response createCoordinates(@Valid final CoordinatesForm form, @Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					Coordinates coordinates = coordinatesService
							.create(form.getPubId(), form.getLatitude().doubleValue(), form.getLongitude().doubleValue());
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/coordinates")
							.path(String.valueOf(coordinates.getId()))
							.build();
					return Response.created(location).entity(new IdDto().id(coordinates.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT 
	@Path("/coordinates/{id}")
	public Response updateCoordinates(@Valid final CoordinatesForm form, @PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));	
					return coordinatesService.findById(id)
							.map(coordinates -> {
								coordinatesService.update(
										id, form.getPubId(), form.getLatitude().doubleValue(), form.getLongitude().doubleValue());
								return Response.noContent().build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET 
	@Path("/coordinates/{id}")
	public Response getCoordinates(@PathParam("id") final long id) {
		return coordinatesService.findById(id)
				.map(coordinates -> {
					return Response.ok().entity(coordinates).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET 
	@Path("/coordinates/pub/{id}")
	public Response getCoordinatesByPub(@PathParam("id") final long id) {
		return pubService.findById(id)
				.map(pub -> {
					return coordinatesService.findByPubId(id)
							.map(coordinates -> {
								return Response.ok().entity(coordinates).build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@DELETE 
	@Path("/coordinates/{id}")
	public Response deleteCoordinates(@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					coordinatesService.findById(id).ifPresent(coordinates -> {
						PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(coordinates.getPubId()));
						coordinatesService.delete(id);
					});
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
