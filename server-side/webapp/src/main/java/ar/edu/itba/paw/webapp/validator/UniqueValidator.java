package ar.edu.itba.paw.webapp.validator;

import ar.edu.itba.paw.interfaces.functional.Deduplicator;
import ar.edu.itba.paw.webapp.constraint.Unique;
import ar.edu.itba.paw.webapp.support.SecuritySupport;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.ApplicationContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

	protected Deduplicator deduplicator;
	protected String key;

	@Inject
	protected ApplicationContext context;

	@Override
	public void initialize(final Unique constraintAnnotation) {
		this.key = constraintAnnotation.key().trim();
		this.deduplicator = context.getBean(constraintAnnotation.source());
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		return SecuritySupport.getPrincipal()
				.map(auth -> !deduplicator.isDuplicated(Optional.of(auth.getId()), key, value))
				.orElse(!deduplicator.isDuplicated(Optional.empty(), key, value));
	}
	
}
