package ar.edu.itba.paw.model.error;

import ar.edu.itba.paw.model.support.GetOutException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;

	/**
	* <p>Especialización que permite reportar errores sobre entidades
	* serializadas en formato JSON incorrectamente.</p>
	*/

public class JSONError extends GetOutError {

	protected final int line;
	protected final int column;

	public JSONError(final JsonProcessingException exception) {
		super(GetOutException.of(ErrorType.JSON_ENTITY_MALFORMED), new StringBuilder()
				.append(ErrorType.JSON_ENTITY_MALFORMED.getMessage())
				.append(" El parser interno informa: [")
				.append(exception.getOriginalMessage())
				.append("].")
				.toString());
		final JsonLocation location = exception.getLocation();
		this.line = location.getLineNr();
		this.column = location.getColumnNr();
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
}
