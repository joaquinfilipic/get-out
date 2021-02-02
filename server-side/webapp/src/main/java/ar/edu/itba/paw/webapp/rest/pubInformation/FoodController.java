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
import ar.edu.itba.paw.interfaces.service.pubInformation.PubFoodService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.information.PubFood;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.form.pub.information.FoodForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class FoodController {
	
	private final PubFoodService foodService;
	private final PubService pubService;
	
	@Inject
	public FoodController(final PubFoodService foodService, final PubService pubService) {
		this.foodService = foodService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/food")
	public Response createFood(@Valid final FoodForm form, @Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					PubFood food = foodService
							.create(form.getPubId(), form.getFood(), form.getPrice().doubleValue());
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/food")
							.path(String.valueOf(food.getId()))
							.build();
					return Response.created(location).entity(new IdDto().id(food.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT 
	@Path("/food/{id}")
	public Response updateFood(@Valid final FoodForm form, @PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					return foodService.findById(id)
							.map(food -> {
								foodService.update(
										id, form.getPubId(), form.getFood(), form.getPrice().doubleValue());
								return Response.noContent().build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/food/{id}")
	public Response getFood(@PathParam("id") final long id) {
		return foodService.findById(id)
				.map(food -> {
					return Response.ok().entity(food).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET
	@Path("/food/pub/{id}")
	public Response getFoodsByPub(@PathParam("id") final long id) {
		return pubService.findById(id)
				.map(pub -> {
					return Response.ok().entity(foodService.listFoodByPub(id)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@DELETE
	@Path("/food/{id}")
	public Response deleteFood(@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					foodService.findById(id).ifPresent(food -> {
						PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(food.getPubId()));
						foodService.delete(id);
					});
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}