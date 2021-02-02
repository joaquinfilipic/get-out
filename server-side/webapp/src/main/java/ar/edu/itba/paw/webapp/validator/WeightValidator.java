package ar.edu.itba.paw.webapp.validator;

import ar.edu.itba.paw.webapp.constraint.Weight;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeightValidator implements ConstraintValidator<Weight, byte[]> {

	protected long weight;

	@Override
	public void initialize(final Weight constraintAnnotation) {
		this.weight = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(final byte[] file, final ConstraintValidatorContext context) {
		if (file == null) return true;
		else return file.length <= weight;
	}
}
