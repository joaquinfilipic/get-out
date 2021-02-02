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
import ar.edu.itba.paw.interfaces.service.pubInformation.PubInfoService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.information.PubInfo;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.form.pub.information.PubInfoForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class PubInfoController {
	
	private final PubInfoService pubInfoService;
	private final PubService pubService;
	
	@Inject
	public PubInfoController(final PubInfoService pubInfoService, final PubService pubService) {
		this.pubInfoService = pubInfoService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/pub/information")
	public Response createPubInformation(@Valid final PubInfoForm form, @Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					PubInfo pubInfo = pubInfoService
							.create(form.getPubId(), form.getAddress(), form.getPrice().doubleValue());
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/pub/information")
							.path(String.valueOf(pubInfo.getId()))
							.build();
					return Response.created(location).entity(new IdDto().id(pubInfo.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT 
	@Path("/pub/information/{id}")
	public Response updatePubInformation(@Valid final PubInfoForm form, @PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					return pubInfoService.findById(id)
							.map(pubInfo -> {
								pubInfoService.update(
										id, form.getPubId(), form.getAddress(), form.getPrice().doubleValue());
								return Response.noContent().build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET 
	@Path("/pub/information/{id}")
	public Response getPubInformation(@PathParam("id") final long id) {
		return pubInfoService.findById(id)
				.map(pubInfo -> {
					return Response.ok().entity(pubInfo).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET
	@Path("/pub/information/pub/{id}")
	public Response getPubInformationByPub(@PathParam("id") final long id) {
		return pubService.findById(id)
				.map(pub -> {
					return pubInfoService.findByPubId(id)
							.map(pubInfo -> {
								return Response.ok().entity(pubInfo).build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@DELETE 
	@Path("/pub/information/{id}")
	public Response deletePubInformation(
			@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					pubInfoService.findById(id).ifPresent(pubInfo -> {
						PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(pubInfo.getPubId()));
						pubInfoService.delete(id);
					});
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
