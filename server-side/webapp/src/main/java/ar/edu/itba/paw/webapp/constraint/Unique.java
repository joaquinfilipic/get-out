package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import ar.edu.itba.paw.interfaces.functional.Deduplicator;
import ar.edu.itba.paw.webapp.validator.UniqueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, METHOD, PARAMETER,
	CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE
})
public @interface Unique {

	String message() default "{ar.edu.itba.paw.webapp.constraint.Unique.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};

	/** La clave a utilizar durante la búsqueda de unicidad. */
	String key();

	/** La fuente que provee un mecanismo para determinar unicidad. */
	Class<? extends Deduplicator> source();
}
