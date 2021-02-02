package ar.edu.itba.paw.webapp.validator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ar.edu.itba.paw.webapp.constraint.ValidImage;

public class ValidImageValidator implements ConstraintValidator<ValidImage, byte[]> {

	@Override
	public boolean isValid(byte[] value, ConstraintValidatorContext context) {
		InputStream inputStream = new ByteArrayInputStream(value);
		try {
			if (ImageIO.read(inputStream) == null) {
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
