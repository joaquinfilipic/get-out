package ar.edu.itba.paw.model.error;

import ar.edu.itba.paw.model.support.GetOutException;

public class GetOutError {

	protected final String error;
	protected final String message;

	public GetOutError(final GetOutException exception, final String message) {
		this.error = exception.getErrorType().getName();
		this.message = message;
	}

	public GetOutError(final GetOutException exception) {
		this(exception, exception.getMessage());
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}
}
