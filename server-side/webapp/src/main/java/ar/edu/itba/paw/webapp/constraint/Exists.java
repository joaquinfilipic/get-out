package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.webapp.validator.ExistsValidator;

@Constraint(validatedBy = ExistsValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, METHOD, PARAMETER,
	CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE
})
public @interface Exists {
	
	String message() default "{ar.edu.itba.paw.webapp.constraint.Exists.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	
	Class<? extends ExistenceCheckable> source();

}
