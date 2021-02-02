package ar.edu.itba.paw.model.error;

import com.fasterxml.jackson.annotation.JsonGetter;
import javax.validation.Path;

public class Violation {

	protected final Path path;
	protected final String explanation;

	public Violation(final Path path, final String explanation) {
		this.path = path;
		this.explanation = explanation;
	}

	@JsonGetter("path")
	public String getPath() {
		return path.toString();
	}

	public String getExplanation() {
		return explanation;
	}
}
