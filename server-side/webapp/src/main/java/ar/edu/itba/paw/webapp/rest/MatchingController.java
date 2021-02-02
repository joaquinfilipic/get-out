package ar.edu.itba.paw.webapp.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonView;

import ar.edu.itba.paw.interfaces.service.AttendanceService;
import ar.edu.itba.paw.interfaces.service.MatchingService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.dto.CountDto;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.dto.factory.ClientDtoFactory;
import ar.edu.itba.paw.webapp.dto.factory.PendingsDtoFactory;
import ar.edu.itba.paw.webapp.dto.model.profile.ClientDto;
import ar.edu.itba.paw.webapp.dto.model.relation.PendingsDto;
import ar.edu.itba.paw.webapp.form.relation.LikeForm;
import ar.edu.itba.paw.webapp.form.relation.RejectForm;
import ar.edu.itba.paw.webapp.param.LocalDateParam;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;

import static java.lang.String.format;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class MatchingController {
	
	private static final Logger log = LoggerFactory.getLogger(MatchingController.class);

	private final MatchingService matchingService;
	private final AttendanceService attendanceService;

	@Inject
	public MatchingController(final MatchingService matchingService, final AttendanceService attendanceService) {
		this.matchingService = matchingService;
		this.attendanceService = attendanceService;
	}

	@POST
	@Path("/like")
	@JsonView(View.Public.class)
	public Response createLike(@Valid final LikeForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("CREATING LIKE -> Sender: %s\tReceiver: %d\tPub: %d\tDate: %s.",
							auth.getUsername(), form.getUserId(), form.getPubId(), form.getDate().toString()));
					if (matchingService.findLike(
							auth.getId(), form.getUserId(), form.getPubId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The sender already liked the receiver for the given event.");
						throw GetOutException.of(ErrorType.DUPLICATED_ENTITY);
					}
					if (!attendanceService.findByPubAndUserAndDate(
							form.getPubId(), auth.getId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The user does not have an attendance for the given event.");
						throw GetOutException.of(ErrorType.ATTENDANCE_REQUIRED);
					}
					if (auth.getId().equals(form.getUserId())) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The sender and receiver are the same user. This is not allowed.");
						throw GetOutException.of(ErrorType.CANNOT_INTERACT_WITH_SELF);
					}
					IdDto response = new IdDto().id(matchingService.createLike(
							auth.getId(), form.getUserId(), form.getPubId(), form.getDate()).getId());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Like created with id %d.", response.getId()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

	@POST
	@Path("/reject")
	@JsonView(View.Public.class)
	public Response createReject(@Valid final RejectForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("CREATING REJECT -> Sender: %s\tReceiver: %d\tPub: %d\tDate: %s.",
							auth.getUsername(), form.getUserId(), form.getPubId(), form.getDate().toString()));
					if (matchingService.findReject(
							auth.getId(), form.getUserId(), form.getPubId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The sender already rejected the receiver for the given event.");
						throw GetOutException.of(ErrorType.DUPLICATED_ENTITY);
					}
					if (!attendanceService.findByPubAndUserAndDate(
							form.getPubId(), auth.getId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The user does not have an attendance for the given event.");
						throw GetOutException.of(ErrorType.ATTENDANCE_REQUIRED);
					}
					if (auth.getId().equals(form.getUserId())) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The sender and receiver are the same user. This is not allowed.");
						throw GetOutException.of(ErrorType.CANNOT_INTERACT_WITH_SELF);
					}
					IdDto response = new IdDto().id(matchingService.createReject(
							auth.getId(), form.getUserId(), form.getPubId(), form.getDate()).getId());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Reject created with id %d.", response.getId()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

	@GET
	@Path("/potential/matches/pub/{pubId}/date/{date}")
	@JsonView(View.Retrieval.class)
	public Response getPotentialMatches(
			@PathParam("pubId") final long pubId,
			@PathParam("date") final LocalDateParam date) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("POTENTIAL MATCHES REQUEST -> User: %s\tPub: %d\tDate: %s.", 
							auth.getUsername(), pubId, date.getDate().toString()));
					if (!attendanceService.findByPubAndUserAndDate(
							pubId, auth.getId(), date.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The user does not have an attendance to the given pub in the given date.");
						throw GetOutException.of(ErrorType.ATTENDANCE_REQUIRED);
					}
					List<ClientDto> response = ClientDtoFactory.buildListFromClients(
							matchingService.getCandidates(auth.getId(), pubId, date.getDate()));
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d potential matches.", response.size()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/pendings")
	@JsonView(View.Public.class)
	public Response getPendings() {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("PENDINGS REQUEST -> User: %s", auth.getUsername()));
					List<PendingsDto> pendingsList = attendanceService.getAttendanceByUser(auth.getId()).stream()
							.map(attendance -> {
								log.info(format("Getting pendings for Pub:[ Id: %d\tName: %s ]\tDate: %s.", 
										attendance.getPub().getId(), attendance.getPub().getName(), attendance.getDate().toString()));
								return PendingsDtoFactory.buildFromAttendanceAndClients(attendance, 
										matchingService.getPendings(auth.getId(), attendance.getPub().getId(), attendance.getDate()));
							}).collect(Collectors.toList());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d pendings.", pendingsList.size()));
					return Response.ok().entity(pendingsList).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/pendings/count")
	public Response getPendingsCount() {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("PENDINGS COUNT REQUEST -> User: %s.", auth.getUsername()));
					Long count = attendanceService.getAttendanceByUser(auth.getId()).stream()
							.map(attendance -> {
								return matchingService.getPendings(
										auth.getId(), attendance.getPub().getId(), attendance.getDate()).size();
							})
							.reduce((a, b) -> a + b)
							.orElse(0).longValue();
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d pendings.", count));
					return Response.ok().entity(new CountDto().count(count)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/pendings/pub/{pubId}/date/{date}")
	@JsonView(View.Public.class)
	public Response getPubPendings(
			@PathParam("pubId") long pubId,
			@PathParam("date") final LocalDateParam date) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("PUB PENDINGS REQUEST -> User: %s\tPub: %d\tDate: %s.", 
							auth.getUsername(), pubId, date.getDate().toString()));
					if (!attendanceService.findByPubAndUserAndDate(
							pubId, auth.getId(), date.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> The user does not have an attendance to the given pub in the given date.");
						throw GetOutException.of(ErrorType.ATTENDANCE_REQUIRED);
					}
					List<ClientDto> response = ClientDtoFactory.buildListFromClients(
							matchingService.getPendings(auth.getId(), pubId, date.getDate()));
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d pendings.", response.size()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
	@GET
	@Path("/matches/pub/{pubId}/date/{date}")
	@JsonView(View.Retrieval.class)
	public Response getMatches(
			@PathParam("pubId") long pubId,
			@PathParam("date") final LocalDateParam date) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("MATCHES REQUEST -> User: %s\tPub: %d\tDate: %s.",
							auth.getUsername(), pubId, date.getDate().toString()));
					if (!attendanceService.findByPubAndUserAndDate(
							pubId, auth.getId(), date.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> No attendance was found for the given pub in the given date.");
						throw GetOutException.of(ErrorType.RESOURCE_NOT_FOUND);
					}
					List<ClientDto> response = ClientDtoFactory.buildListFromClients(
							matchingService.getMatches(auth.getId(), pubId, date.getDate()));
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d matches.", response.size()));
					return Response.ok().entity(response).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}

}
