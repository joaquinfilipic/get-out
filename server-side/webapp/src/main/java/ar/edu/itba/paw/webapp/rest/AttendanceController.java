package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.service.AttendanceService;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.model.support.GetOutException;
import ar.edu.itba.paw.model.support.View;
import ar.edu.itba.paw.webapp.dto.IdDto;
import ar.edu.itba.paw.webapp.dto.BooleanResponseDto;
import ar.edu.itba.paw.webapp.dto.CountDto;
import ar.edu.itba.paw.webapp.dto.factory.AttendanceDtoFactory;
import ar.edu.itba.paw.webapp.form.relation.CreateAttendanceForm;
import ar.edu.itba.paw.webapp.param.LocalDateParam;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import ar.edu.itba.paw.webapp.support.SecuritySupport;
import com.fasterxml.jackson.annotation.JsonView;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import static java.lang.String.format;

import java.util.List;

@Controller
@Path("/v1.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(GetOutMediaType.APPLICATION_GETOUT_v1_JSON)
public class AttendanceController {

	private static final Logger log = LoggerFactory.getLogger(AttendanceController.class);

	private final AttendanceService attendanceService;
	private final PubService pubService;
	
	@Inject
	public AttendanceController(final AttendanceService attendanceService, final PubService pubService) {
		this.attendanceService = attendanceService;
		this.pubService = pubService;
	}
	
	@POST 
	@Path("/attendance")
	@JsonView(View.Public.class)
	public Response attendPub(@Valid final CreateAttendanceForm form) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("ATTENDING PUB -> Pub: %d\tUser: %s\tDate: %s.", form.getPubId(), auth.getUsername(), form.getDate().toString()));
					if (attendanceService.findByPubAndUserAndDate(
							form.getPubId(), auth.getId(), form.getDate()).isPresent()) {
						log.error("----------------------------------------------------------------------------------------------------");
						log.error("ERROR -> Duplicated entity.");
						throw GetOutException.of(ErrorType.DUPLICATED_ENTITY);
					}
					Attendance attendance = attendanceService.create(form.getPubId(), auth.getId(), form.getDate());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> attendance created. Id: %d", attendance.getId()));
					return Response.ok().entity(new IdDto().id(attendance.getId())).build();
				})
				.orElseThrow(() -> {
					log.error("----------------------------------------------------------------------------------------------------");
					log.error("ERROR -> Could not authenticate the invoker when trying to attend a pub.");
					return GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN);
				});
	}
	
	@GET
	@Path("/attendance/validate/pub/{pubId}/user/{userId}/date/{date}")
	public Response validateAttendance(
			@PathParam("pubId") final long pubId,
			@PathParam("userId") final long userId,
			@PathParam("date") final LocalDateParam date) {
		log.info("****************************************************************************************************");
		log.info(format("VALIDATING ATTENDANCE -> Pub: %d\tUser: %d\tDate: %s.", pubId, userId, date.getDate().toString()));
		Boolean response = attendanceService.findByPubAndUserAndDate(pubId, userId, date.getDate()).isPresent();
		log.info("----------------------------------------------------------------------------------------------------");
		log.info(format("RESPONSE -> Attendance existance check returned: %s.", response.toString()));
		return Response.ok().entity(new BooleanResponseDto().response(response)).build();
	}
	
	@GET
	@Path("/attendance/pub/{id}/date/{date}")
	public Response getAttendanceCount(
			@PathParam("id") final long id,
			@PathParam("date") final LocalDateParam date) {
		log.info("****************************************************************************************************");
		log.info(format("REQUEST ATTENDANCE COUNT -> Pub: %d\tDate: %s.", id, date.getDate().toString()));
		return pubService.findById(id)
				.map(pub -> {
					Long count = attendanceService.getPubAttendanceCount(id, date.getDate());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Count: %d.", count));
					return Response.ok().entity(new CountDto().count(count)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.RESOURCE_NOT_FOUND));
	}
	
	@GET @Path("/attendance")
	@JsonView(View.Public.class)
	public Response getUserAttendance() {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("USER ATTENDANCES REQUEST -> User: [Id: %d\tUsername: %s].", auth.getId(), auth.getUsername()));
					List<Attendance> attendanceList = attendanceService.getAttendanceByUser(auth.getId());
					log.info("----------------------------------------------------------------------------------------------------");
					log.info(format("RESPONSE -> Found %d attendances.", attendanceList.size()));
					return Response.ok().entity(AttendanceDtoFactory.buildFromAttendanceList(attendanceList)).build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
		
	@DELETE 
	@Path("/attendance/{id}")
	public Response deleteAttendance(@PathParam("id") final long id) {
		return SecuritySupport.getPrincipal()
				.map(auth -> {
					log.info("****************************************************************************************************");
					log.info(format("DELETING ATTENDANCE -> User: [Id: %d\tUsername: %s]\tAttendance: %d.", 
							auth.getId(), auth.getUsername(), id));
					attendanceService.findById(id).ifPresent(attendance -> {
						if (!attendance.getUser().getId().equals(auth.getId())) {
							log.error("----------------------------------------------------------------------------------------------------");
							log.error("ERROR -> The attendance exists but belongs to another user.");
							throw GetOutException.of(ErrorType.BELONGS_TO_OTHER);
						}
						log.info(format("Deleting attendance. User: %s\tPub: %d\tDate: %s.", 
								auth.getUsername(), attendance.getPub().getId(), attendance.getDate().toString()));
						attendanceService.delete(id);
					});
					log.info("----------------------------------------------------------------------------------------------------");
					log.info("RESPONSE -> Returning OK, whether the attendance was deleted or did not exist.");
					return Response.noContent().build();
				})
				.orElseThrow(() -> GetOutException.of(ErrorType.EMPTY_AUTHENTICATION_TOKEN));
	}
	
}
