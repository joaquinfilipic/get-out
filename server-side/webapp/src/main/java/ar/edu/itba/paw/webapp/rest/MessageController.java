package ar.edu.itba.paw.webapp.rest;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonView;

import ar.edu.itba.paw.interfaces.service.AttendanceService;
import ar.edu.itba.paw.interfaces.service.MessageService;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.model.chat.PubMessage;
import ar.edu.itba.paw.model.chat.UserMessage;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.form.chat.CreatePubMessageForm;
import ar.edu.itba.paw.webapp.form.chat.CreateUserMessageForm;
import ar.edu.itba.paw.webapp.param.LocalDateParam;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class MessageController {
	
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);

	private final MessageService messageService;
	private final PubService pubService;
	private final AttendanceService attendanceService;

	@Inject
	public MessageController(final MessageService messageService, final PubService pubService,
			final AttendanceService attendanceService) {
		this.messageService = messageService;
		this.pubService = pubService;
		this.attendanceService = attendanceService;
	}

	@POST 
	@Path("/messages/global")
	public Response createPubMessage(@Valid final CreatePubMessageForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("CREATING PUB MESSAGE -> User: %d\tPub: %d\tDate: %s.",
							auth.getId(), form.getPubId(), form.getDate().toString()));
					if (!attendanceService.findByPubAndUserAndDate(
							form.getPubId(), auth.getId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The user does not have an attendance for the given event.");
						throw GetOutException.of(ErrorType.ATTENDANCE_REQUIRED);
					}
					PubMessage message = messageService.sendToPub(
							form.getPubId(), auth.getId(), form.getMessage(), form.getDate());
					log.info(format("RESPONSE -> Message saved. Timestamp: %s.", message.getTimestamp().toString()));
					return Response.ok().entity(message).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@POST 
	@Path("/messages/private")
	public Response createUserMessage(@Valid final CreateUserMessageForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("CREATING PRIVATE MESSAGE -> Sender: %d\tReceiver: %d.", auth.getId(), form.getUserId()));
					if (auth.getId().equals(form.getUserId())) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The sender and receiver of the message are the same person. This is not allowed.");
						throw GetOutException.of(ErrorType.CANNOT_INTERACT_WITH_SELF);
					}
					UserMessage message = messageService.sendToUser(
							form.getUserId(), auth.getId(), form.getMessage());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Message saved. Timestamp: %s.", message.getTimestamp().toString()));
					return Response.ok().entity(message).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/messages/pub/{pubId}/date/{date}")
	@JsonView(View.Retrieval.class)
	public Response getPubMessages(
			@PathParam("pubId") final long pubId,
			@PathParam("date") final LocalDateParam date,
			@QueryParam("fromId") final Long fromId) {
		return pubService.findById(pubId)
				.map(pub -> {
					log.info("****************************************************************************************************");
					log.info(format("PUB MESSAGES REQUEST -> Pub: %d\tDate: %s\tLast Message Id: %d.", 
							pubId, date.getDate().toString(), fromId));
					List<PubMessage> response = messageService.receiveFromPub(pubId, Optional.ofNullable(fromId), date.getDate());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Returning %d pub messages.", response.size()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> {
					log.error("----------------------------------------------------------------------------------------------------");
					log.error("ERROR -> Could not find a pub with the given id.");
					return GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
				});		
	}
	
	@GET 
	@Path("/messages/user/{id}")
	@JsonView(View.Retrieval.class)
	public Response getUserMessages(
			@PathParam("id") final long id,
			@QueryParam("fromId") final Long fromId) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("PRIVATE MESSAGES REQUEST -> Requester: %d\tUser: %d.", auth.getId(), id));
					if (auth.getId().equals(id)) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The requester and the user of the request are the same person. This is not allowed.");
						throw GetOutException.of(ErrorType.CANNOT_INTERACT_WITH_SELF);
					}
					List<UserMessage> response = messageService.receiveFromUser(
							id, auth.getId(), Optional.ofNullable(fromId));
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Returning %d private messages.", response.size()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
