package ar.edu.itba.paw.webapp.rest;

import static java.lang.String.format;
import ar.edu.itba.paw.interfaces.auth.JWTAuthenticationService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.dto.factory.UserDtoFactory;
import ar.edu.itba.paw.webapp.dto.model.profile.TokenDto;
import ar.edu.itba.paw.webapp.form.user.CreateUserForm;
import ar.edu.itba.paw.webapp.form.user.LoginForm;
import ar.edu.itba.paw.webapp.form.user.PasswordForm;
import ar.edu.itba.paw.webapp.form.user.UserForm;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final JWTAuthenticationService jwtService;
	private final UserService userService;

	@Inject
	public UserController(final JWTAuthenticationService jwtService, final UserService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@POST
	@Path("/users")
	public Response createUser(@Valid final CreateUserForm form, @Context final UriInfo uri) {
		
		final UserForm userForm = form.getUserForm();
		final PasswordForm passwordForm = form.getPasswordForm();

		log.info("****************************************************************************************************");
		log.info(format("CREATING USER -> Name: %s\tUsername: %s\tMail: %s.",
				userForm.getName(), userForm.getUsername(), userForm.getMail()));

		User user = userService.create(userForm.getUsername(), passwordForm.getPassword(),
				userForm.getName(), userForm.getMail());

		log.info("----------------------------------------------------------------------------------------------------");
		log.info(format("RESPONSE -> User created. Id: %d.", user.getId()));
		
		final URI location = uri.getBaseUriBuilder()
				.path("/v1.0/user")
				.path(String.valueOf(user.getId()))
				.build();
		final User loggedUser = userService.login(user.getUsername(), passwordForm.getPassword());
		final String token = jwtService.createToken(loggedUser);
		return Response.created(location).entity(new TokenDto().id(user.getId()).token(token)).build();
	}
	
	@PUT
	@Path("/password")
	public Response updatePassword(@Valid final PasswordForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("UPDATING PASSWORD -> User: %d.", auth.getId()));
					userService.updatePassword(auth.getId(), form.getPassword());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> Password updated (whether the information changed or not).");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

	@POST
	@Path("/login")
	public Response login(@Valid final LoginForm form) {
		log.info("****************************************************************************************************");
		log.info(format("LOGIN REQUEST -> Username: %s.", form.getUsername()));
		final User user = userService.login(form.getUsername(), form.getPassword());
		final String token = jwtService.createToken(user);
		log.info("----------------------------------------------------------------------------------------------------");
		log.info(format("RESPONSE -> Token generated for user: [Id: %d\tUsername: %s].", user.getId(), user.getUsername()));
		return Response.ok(new TokenDto().id(user.getId()).token(token)).build();
	}

	@GET
	@Path("/user")
	@JsonView(View.Retrieval.class)
	public Response getMyself() {
		return SecuritySupport.getPrincipal()
				.map(auth -> getUser(auth.getId()))
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

	@GET
	@Path("/user/{id}")
	@JsonView(View.Public.class)
	public Response getUser(@PathParam("id") final long id) {
		log.info("****************************************************************************************************");
		log.info(format("USER REQUEST -> User: %d.", id));
		return userService.findById(id)
				.map(user -> {
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Returning found user: [Id: %d\tName: %s\tUsername: %d].",
							user.getId(), user.getName(), user.getUsername()));
					return Response.ok().entity(UserDtoFactory.buildFromUser(user)).build();
				})
				.orElseThrow(() -> {
					log.error("----------------------------------------------------------------------------------------------------");
					log.error("ERROR -> Could not find a user with the given id.");
					return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
				});
	}

	@POST
	@Path("/logout")
	public Response logout(@Context final HttpServletRequest request) {
		userService.logout(request);
		return Response.ok().build();
	}

	@DELETE
	@Path("/user")
	public Response deleteUser(@Context final HttpServletRequest request) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("DELETING USER -> User: [Id: %d\tUsername: %s].", auth.getId(), auth.getUsername()));
					userService.logout(request);
					userService.delete(auth.getId());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> User deleted.");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
}
