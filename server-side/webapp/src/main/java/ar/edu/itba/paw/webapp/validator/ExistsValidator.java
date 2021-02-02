package ar.edu.itba.paw.webapp.validator;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.ApplicationContext;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.webapp.constraint.Exists;

public class ExistsValidator implements ConstraintValidator<Exists, Long> {

	private ExistenceCheckable existenceCheckable;

	@Inject
	private ApplicationContext context;

	@Override
	public void initialize(final Exists constraintAnnotation) {
		this.existenceCheckable = context.getBean(constraintAnnotation.source());
	}

	@Override
	public boolean isValid(Long id, ConstraintValidatorContext context) {
		return existenceCheckable.exists(id);
	}

}