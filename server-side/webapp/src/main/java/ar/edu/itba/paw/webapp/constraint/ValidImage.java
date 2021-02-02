package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ar.edu.itba.paw.webapp.validator.ValidImageValidator;

@Constraint(validatedBy = ValidImageValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, PARAMETER
})
public @interface ValidImage {
	
	String message() default "{ar.edu.itba.paw.webapp.constraint.Image.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	
}
