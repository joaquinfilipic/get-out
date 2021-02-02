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
import ar.edu.itba.paw.interfaces.service.pubInformation.PromoService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.information.Promo;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.form.pub.information.PromoForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import ar.edu.itba.paw.webapp.validator.PubOwnerValidator;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class PromoController {
	
	private final PromoService promoService;
	private final PubService pubService;
	
	@Inject
	public PromoController(final PromoService promoService, final PubService pubService) {
		this.promoService = promoService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/promos")
	public Response createPromo(@Valid final PromoForm form, @Context final UriInfo uri) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					Promo promo = promoService.create(form.getPubId(), form.getName(), form.getDescription());
					final URI location = uri.getBaseUriBuilder()
							.path("/v1.0/promos")
							.path(String.valueOf(promo.getId()))
							.build();
					return Response.created(location).entity(new IdDto().id(promo.getId())).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT 
	@Path("/promos/{id}")
	public Response updatePromo(@Valid final PromoForm form, @PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(form.getPubId()));
					return promoService.findById(id)
							.map(promo -> {
								promoService.update(id, form.getPubId(), form.getName(), form.getDescription());
								return Response.noContent().build();
							})
							.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET 
	@Path("/promos/{id}")
	public Response getPromo(@PathParam("id") final long id) {
		return promoService.findById(id)
				.map(promo -> {
					return Response.ok().entity(promo).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET 
	@Path("/promos/pub/{id}")
	public Response getPromosByPub(@PathParam("id") final long id) {
		return pubService.findById(id)
				.map(pub -> {
					return Response.ok().entity(promoService.listPromosByPub(id)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@DELETE 
	@Path("/promos/{id}")
	public Response deletePromo(@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					promoService.findById(id).ifPresent(promo -> {
						PubOwnerValidator.validateOwnership(auth.getId(), pubService.findById(promo.getPubId()));
						promoService.delete(id);
					});
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
