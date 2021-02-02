package ar.edu.itba.paw.webapp.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ar.edu.itba.paw.webapp.constraint.Enum;

public class EnumValidator implements ConstraintValidator<Enum, String> {

	private Enum annotation;
	
	@Override
	public void initialize(final Enum annotation) {
		this.annotation = annotation;
	}
	
	@Override
	public boolean isValid(final String value, ConstraintValidatorContext context) {
				
		if (value == null) {
			return true;
		}
		
		Object[] enumValues = this.annotation.enumClass().getEnumConstants();
		
		if (enumValues != null) {
			for (Object enumValue : enumValues) {
				if (value.equals(enumValue.toString())
						|| (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
					return true;
				}
			}
		}
		
		return false;		
	}
	
}
