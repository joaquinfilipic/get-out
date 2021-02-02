package ar.edu.itba.paw.model.support;

import ar.edu.itba.paw.model.error.ErrorType;
import javax.ws.rs.core.Response.Status;

	/**
	* <p>Excepción general de la aplicación web GetOut©. Permite representar
	* errores propios del sistema o enmascarar otro tipo de excepciones dentro
	* de esta.</p>
	*/

public class GetOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected final ErrorType type;

	public GetOutException(final ErrorType type) {
		super(type.getMessage());
		this.type = type;
	}

	public static GetOutException of(final ErrorType type) {
		return new GetOutException(type);
	}

	public Status getStatus() {
		return type.getStatus();
	}

	public ErrorType getErrorType() {
		return type;
	}
}
