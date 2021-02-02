package ar.edu.itba.paw.model.error;

import javax.ws.rs.core.Response.Status;

	/**
	* <p>Contiene todos los errores específicos de la aplicación web. En
	* general, cada error pertenece a una familia (indicada mediante un
	* status-code HTTP), y una subfamilia (propiedad <i>name()</i>). Esto
	* permite reportar errores más específicos bajo los mismos status-code del
	* estándar.</p>
	*/

public enum ErrorType {

	// Errores asociados a la familia 4xx.
	JSON_ENTITY_MALFORMED(
			Status.BAD_REQUEST,
			"La entidad suministrada no se corresponde con un objeto JSON."),
	CONSTRAINT_VIOLATION(
			Status.BAD_REQUEST,
			"La entidad suministrada no aprobó el proceso de validación por completo."),
	INVALID_PATH_PARAMETER(
			Status.BAD_REQUEST,
			"Los parámetros en el path (path-params), son inválidos."),
	DUPLICATED_ENTITY(
			Status.BAD_REQUEST,
			"La entidad suministrada ya existe."),
	NON_EXISTENT_ENTITY(
			Status.BAD_REQUEST,
			"Un ID proporcionado corresponde a una entidad que no existe."),
	CANNOT_INTERACT_WITH_SELF(
			Status.BAD_REQUEST,
			"No está permitido interactuar socialmente con uno mismo."),
	EMPTY_AUTHENTICATION_TOKEN(
			Status.UNAUTHORIZED,
			"Debe proveer un token de autenticación para acceder. " +
			"El token no está, o el header 'Authorization: Bearer <token>' está malformado."),
	INVALID_AUTHENTICATION_TOKEN(
			Status.UNAUTHORIZED,
			"El token de autenticación proporcionado es inválido."),
	AUTHENTICATION_TOKEN_EXPIRED(
			Status.UNAUTHORIZED,
			"El token de autenticación ha expirado. Debe renovarlo."),
	BAD_CREDENTIALS(
			Status.UNAUTHORIZED,
			"Las credenciales proporcionadas son inválidas. Inténtelo nuevamente."),
	ATTENDANCE_REQUIRED(
			Status.FORBIDDEN,
			"La acción requiere confirmación de asistencia previa."),
	MATCH_REQUIRED(
			Status.FORBIDDEN,
			"La acción requiere emparejamiento previo entre los usuarios involucrados."),
	NO_MATCHING_AUTHENTICATION(
			Status.FORBIDDEN,
			"Solo está permitido realizar acciones correspondientes al usuario con el que recibió la autenticación. " +
			"Verifique que el ID de usuario utilizado sea el correcto."),
	BELONGS_TO_OTHER(
			Status.FORBIDDEN,
			"No puede modificar/eliminar un recurso que pertenece a otro usuario."),
	FORBIDDEN_TOKEN(
			Status.FORBIDDEN,
			"El token que está utilizando ha sido invalidado y su uso está prohibido. Consiga otro token."),
	RESOURCE_NOT_FOUND(
			Status.NOT_FOUND,
			"El recurso solicitado no existe, o el endpoint indicado es incorrecto."),
	METHOD_NOT_ALLOWED(
			Status.METHOD_NOT_ALLOWED,
			"El endpoint solicitado no soporta el verbo HTTP utilizado. Verifique la especificación del API."),
	UNSUPPORTED_MEDIA_TYPE(
			Status.UNSUPPORTED_MEDIA_TYPE,
			"La entidad proporcionada se encuentra en un formato incorrecto. Utilice JSON."),

	// Errores asociados a la familia 5xx.
	UNKNOWN_ERROR(
			Status.INTERNAL_SERVER_ERROR,
			"Se produjo un error inesperado en el servidor. Contacte al soporte técnico."),
	SERVICE_UNAVAILABLE(
			Status.SERVICE_UNAVAILABLE,
			"El servicio no se pudo completar. Inténtelo nuevamente más tarde.");

	private Status status;
	private String message;

	private ErrorType(final Status status, final String message) {
		this.status = status;
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public String getName() {
		return name();
	}

	public String getMessage() {
		return message;
	}
}
