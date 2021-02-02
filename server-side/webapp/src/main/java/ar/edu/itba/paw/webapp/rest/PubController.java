package ar.edu.itba.paw.webapp.rest;

import static java.lang.String.format;
import ar.edu.itba.paw.interfaces.service.AttendanceService;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.dto.BooleanResponseDto;
import ar.edu.itba.paw.webapp.dto.factory.PubDtoFactory;
import ar.edu.itba.paw.webapp.dto.model.PubDto;
import ar.edu.itba.paw.webapp.form.ImageForm;
import ar.edu.itba.paw.webapp.form.pub.CreatePubForm;
import ar.edu.itba.paw.webapp.form.pub.PubForm;
import ar.edu.itba.paw.webapp.param.LocalDateParam;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class PubController {

	private static final Logger log = LoggerFactory.getLogger(PubController.class);

	private final PubService pubService;
	private final AttendanceService attendanceService;
	
	@Inject
	public PubController(final PubService pubService, final AttendanceService attendanceService) {
		this.pubService = pubService;
		this.attendanceService = attendanceService;
	}
	
	@POST
	@Path("/pubs")
	public Response createPub(
			@Valid final CreatePubForm form,
			@Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					final PubForm pubForm = form.getPubForm();
					final ImageForm imageForm = form.getImageForm();
					
					log.info("****************************************************************************************************");
					log.info(format("CREATING PUB -> User: %d\tName: %s\tDescription: %s\tOpenTime: %s.", 
							auth.getId(), pubForm.getName(), pubForm.getDescription(), pubForm.getOpenTime().toString()));
					
					Pub pub = pubService.create(auth.getId(), pubForm.getName(), pubForm.getDescription(), pubForm.getOpenTime(),
								imageForm.getImage());
					
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Pub created. Id: %d.", pub.getId()));
					
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/pub")
							.path(String.valueOf(pub.getId()))
							.build();
					
					return Response.created(location).entity(new IdDto().id(pub.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT
	@Path("/pub/{id}")
	public Response updatePub(
			@Valid final PubForm form,
			@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("UPDATING PUB -> Id: %d\tName: %s\tDescription: %s\tOpenTime: %s\tUser: %d.",
							id, form.getName(), form.getDescription(), form.getOpenTime().toString(), auth.getId()));
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(id));
					return pubService.findById(id)
							.map(pub -> {
								pubService.updatePub(
										id, form.getName(), form.getDescription(), form.getOpenTime());
								log.info("----------------------------------------------------------------------------------------------------");
								log.info("RESPONSE -> Pub updated (whether the information changed or not).");
								return Response.noContent().build();
							})
							.orElseThrow(() -> {
								log.error("----------------------------------------------------------------------------------------------------");
								log.error("ERROR -> Could not find a pub with the given id.");
								return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
							});
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT
	@Path("/pub/{id}/image")
	public Response updateImage(
			@Valid final ImageForm form,
			@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("UPDATING PUB IMAGE -> Pub: %d\tUser: %d\tContent-type: %s.", 
							id, auth.getId(), form.getImage().getContentType().getContentType()));
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(id));
					return pubService.findById(id)
							.map(pub -> {
								pubService.updateImage(id, form.getImage());
								log.info("----------------------------------------------------------------------------------------------------");
								log.info("RESPONSE -> Pub image updated (whether the information changed or not).");
								return Response.noContent().build();
							})
							.orElseThrow(() -> {
								log.error("----------------------------------------------------------------------------------------------------");
								log.error("ERROR -> Could not find a pub with the given id.");
								return GetOutException.of(ErrorType.UNKNOWN_ERROR);
							});
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET 
	@Path("/pub/{id}")
	@JsonView(View.Public.class)
	public Response getPub(@PathParam("id") final long id) {
		log.info("****************************************************************************************************");
		log.info(format("PUB REQUEST -> Pub: %d.", id));
		return pubService.findById(id)
				.map(pub -> {
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Returning pub. Id: %d\tUser: %d\tName: %s\tDescription: %s\tOpenTime: %s.",
							id, pub.getUser().getId(), pub.getName(), pub.getDescription(), pub.getOpenTime()));
					return Response.ok().entity(PubDtoFactory.buildFromPub(pub)).build();
				})
				.orElseThrow(() -> {
					log.error("----------------------------------------------------------------------------------------------------");
					log.error("ERROR -> Could not find a pub with the given id.");
					return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
				});
	}
	
	@GET
	@Path("/pubs/validate/pub/{pubId}/user/{userId}")
	public Response validatePubOwner(
			@PathParam("pubId") final long pubId,
			@PathParam("userId") final long userId) {
		log.info("****************************************************************************************************");
		log.info(format("VALIDATING PUB OWNERSHIP -> Pub: %d\tUser: %d.", pubId, userId));
		Boolean response = pubService.findById(pubId)
				.map(pub -> pub.getUser().getId().equals(userId))
				.orElse(false);
		log.info("----------------------------------------------------------------------------------------------------");
		log.info(format("RESPONSE -> Pub ownership validation returned: %s.", response.toString()));
		return Response.ok().entity(new BooleanResponseDto().response(response)).build();
	}
	
	@GET
	@Path("/pubs/date/{date}")
	@JsonView(View.Public.class)
	public Response getPubsByDate(
			@PathParam("date") final LocalDateParam date,
			@QueryParam("name") final String name) {
		log.info("****************************************************************************************************");
		log.info("PUBS BY DATE REQUEST -> Date: {}\t Name: {}.", date.getDate().toString(), name);
		List<Pub> pubs = new ArrayList<>();
		pubs = (name == null) ? pubService.findPubs() : pubService.findPubsByName(name);
		final List<PubDto> response = pubs.stream()
				.map(pub -> {
					Long pubAttendance = attendanceService.getPubAttendanceCount(pub.getId(), date.getDate());
					return PubDtoFactory.buildFromPub(pub).attendance(pubAttendance);							
				})
				.collect(Collectors.toList());
		log.info("----------------------------------------------------------------------------------------------------");
		log.info(format("RESPONSE -> Returning a total of %d pubs found.", response.size()));
		return Response.ok().entity(response).build();
	}
	
	@DELETE
	@Path("/pub/{id}")
	public Response deletePub(
			@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("DELETING PUB -> Pub: %d\tUser: %d.", id, auth.getId()));
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(id));
					pubService.delete(id);
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> Returning OK, whether the pub was deleted or did not exist.");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
		
}
