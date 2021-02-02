package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import ar.edu.itba.paw.webapp.validator.NonEmptyUploadValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NonEmptyUploadValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, METHOD, PARAMETER,
	CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE
})
public @interface NonEmptyUpload {

	String message() default "{ar.edu.itba.paw.webapp.constraint.NonEmptyUpload.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
}
