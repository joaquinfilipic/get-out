package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import ar.edu.itba.paw.webapp.validator.WeightValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = WeightValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, METHOD, PARAMETER,
	CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE
})
public @interface Weight {

	public static final long MEGABYTE = 1024 * 1024;

	String message() default "{ar.edu.itba.paw.webapp.constraint.Weight.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	long value() default MEGABYTE;
}
