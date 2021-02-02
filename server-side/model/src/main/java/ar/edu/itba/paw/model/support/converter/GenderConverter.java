package ar.edu.itba.paw.model.support.converter;

import ar.edu.itba.paw.model.profile.Gender;
import javax.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Gender, String> {

	@Override
	public String convertToDatabaseColumn(final Gender gender) {
		if (gender != null) {
			return gender.toString();
		}
		return null;
	}

	@Override
	public Gender convertToEntityAttribute(final String gender) {
		if (gender != null) {
			return Gender.fromGender(gender);
		}
		return null;
	}
}
