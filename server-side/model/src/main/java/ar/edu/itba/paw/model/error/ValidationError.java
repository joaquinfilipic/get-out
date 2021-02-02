package ar.edu.itba.paw.model.error;

import ar.edu.itba.paw.model.support.GetOutException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

public class ValidationError extends GetOutError {

	protected final List<Violation> violations;

	public ValidationError(
			final ConstraintViolationException exception) {
		super(GetOutException.of(ErrorType.CONSTRAINT_VIOLATION));
		this.violations = exception.getConstraintViolations()
				.stream()
				.map(v -> new Violation(v.getPropertyPath(), v.getMessage()))
				.collect(Collectors.toList());
	}

	public List<Violation> getViolations() {
		return violations;
	}
}
