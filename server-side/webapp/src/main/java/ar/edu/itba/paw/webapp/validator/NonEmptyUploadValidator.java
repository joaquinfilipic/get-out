package ar.edu.itba.paw.webapp.validator;

import ar.edu.itba.paw.webapp.constraint.NonEmptyUpload;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class NonEmptyUploadValidator
	implements ConstraintValidator<NonEmptyUpload, MultipartFile> {

	@Override
	public void initialize(final NonEmptyUpload constraintAnnotation) {}

	@Override
	public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
		if (file == null) return false;
		else return 0 < file.getSize();
	}
}
