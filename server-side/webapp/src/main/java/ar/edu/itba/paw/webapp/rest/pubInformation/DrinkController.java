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
import ar.edu.itba.paw.interfaces.service.pubInformation.PubDrinkService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.information.PubDrink;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.form.pub.information.DrinkForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class DrinkController {
	
	private final PubDrinkService drinkService;
	private final PubService pubService;
	
	@Inject
	public DrinkController(final PubDrinkService drinkService, final PubService pubService) {
		this.drinkService = drinkService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/drinks")
	public Response createDrink(@Valid final DrinkForm form, @Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					PubDrink drink = drinkService
							.create(form.getPubId(), form.getDrink(), form.getPrice().doubleValue());
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/drinks")
							.path(String.valueOf(drink.getId()))
							.build();
					return Response.created(location).entity(new IdDto().id(drink.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT 
	@Path("/drinks/{id}")
	public Response updateDrink(@Valid final DrinkForm form, @PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					return drinkService.findById(id)
							.map(drink -> {
								drinkService.update(
										id, form.getPubId(), form.getDrink(), form.getPrice().doubleValue());
								return Response.noContent().build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET 
	@Path("/drinks/{id}")
	public Response getDrink(@PathParam("id") final long id) {
		return drinkService.findById(id)
				.map(drink -> {
					return Response.ok().entity(drink).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET 
	@Path("/drinks/pub/{id}")
	public Response getDrinksByPub(@PathParam("id") final long id) {
		return pubService.findById(id)
				.map(pub -> {
					return Response.ok().entity(drinkService.listDrinksByPub(id)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@DELETE 
	@Path("/drinks/{id}")
	public Response deleteDrink(@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					drinkService.findById(id).ifPresent(drink -> {
						PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(drink.getPubId()));
						drinkService.delete(id);
					});
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
