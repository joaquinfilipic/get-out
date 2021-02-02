package ar.edu.itba.paw.model.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

	MALE("MALE"), 
	FEMALE("FEMALE"), 
	OTHER("OTHER");

	private final String gender;

	private Gender(final String gender) {
		this.gender = gender;
	}

	@JsonValue
	public String getGender() {
		return gender;
	}

	@Override
	public String toString() {
		return gender;
	}
	
	@JsonCreator
	public static Gender fromGender(final String gender) {
		if (gender == null) {
			return null;
		}
		for (Gender g : Gender.values()) {
			if (g.toString().toLowerCase().equals(gender.toLowerCase())) {
				return g;
			}
		}
		return null;
	}
	
}
