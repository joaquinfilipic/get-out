package ar.edu.itba.paw.webapp.rest;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonView;

import ar.edu.itba.paw.interfaces.service.ClientService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.form.ImageForm;
import ar.edu.itba.paw.webapp.form.user.LoginForm;
import ar.edu.itba.paw.webapp.form.user.UserForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;

import static java.lang.String.format;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class ClientController {
	
	private static final Logger log = LoggerFactory.getLogger(ClientController.class);
	
	private final UserController userController;
	private final UserService userService;
	private final ClientService clientService;
	
	@Inject
	public ClientController(final UserController userController, final UserService userService, final ClientService clientService) {
		this.userController = userController;
		this.userService = userService;
		this.clientService = clientService;
	}
	
	@PUT
	@Path("/profile")
	public Response updateProfile(@Valid final UserForm form) {	
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("UPDATING PROFILE -> Id: %d\tName: %s\tUsername: %s\tMail: %s\tGender: %s\tBio: %s.",
							auth.getId(), form.getName(), form.getUsername(), form.getMail(), form.getGender().getGender(), form.getBio()));
					boolean userUpdated = userService.updateProfile(auth.getId(), form.getName(), form.getUsername(), form.getMail());
					clientService.updateProfile(auth.getId(), form.getBio(), form.getGender());
					if (userUpdated) {
						User user = userService.findById(auth.getId()).get();
						log.info("----------------------------------------------------------------------------------------------------");
						log.info(format("RESPONSE -> Profile updated. Returning new token for User: [Id: %d\tUsername: %s].",
								user.getId(), user.getUsername()));
						return userController.login(new LoginForm().username(user.getUsername()).password(user.getPassword()));
					};
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> Profile updated (whether the information changed or not) but a new token is not needed.");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@PUT
	@Path("/avatar")
	public Response updateAvatar(@Valid final ImageForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("UPDATING AVATAR -> User: [Id: %d\tUsername: %s]\tContent-type: %s.", 
							auth.getId(), auth.getUsername(), form.getImage().getContentType().getContentType()));
					clientService.updateAvatar(auth.getId(), form.getImage());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> Avatar updated (whether the information changed or not).");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/profile")
	@JsonView(View.Retrieval.class)
	public Response getProfile() {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("PROFILE REQUEST -> User: [Id: %d\tUsername: %s].", auth.getId(), auth.getUsername()));
					return clientService.findById(auth.getId())
							.map(client -> {
								log.info("----------------------------------------------------------------------------------------------------");
								log.info(format("RESPONSE -> Id: %d\tName: %s\tUsername: %s\tMail: %s\tGender: %s\tBio: %s.",
										client.getId(), client.getUser().getName(), client.getUser().getUsername(), 
										client.getUser().getMail(), client.getGender().getGender(), client.getBio()));
								return Response.ok().entity(client).build();
							})
							.orElseThrow(() -> {
								log.error("----------------------------------------------------------------------------------------------------");
								log.error("ERROR -> The profile does not exists.");
								return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
							});
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
