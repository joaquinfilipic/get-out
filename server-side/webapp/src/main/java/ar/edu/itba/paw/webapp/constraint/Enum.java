package ar.edu.itba.paw.webapp.constraint;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ar.edu.itba.paw.webapp.validator.EnumValidator;

@Constraint(validatedBy = EnumValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	FIELD, PARAMETER, LOCAL_VARIABLE
})
public @interface Enum {
	
	String message() default "{ar.edu.itba.paw.webapp.constraint.Enum.message}";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	
	public abstract Class<? extends java.lang.Enum<?>> enumClass();
	public abstract boolean ignoreCase() default false;

}
