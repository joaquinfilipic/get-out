package ar.edu.itba.paw.webapp.dto.factory;

import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.webapp.dto.model.PubDto;

public class PubDtoFactory {
	
	public static PubDto buildFromPub(final Pub pub) {
		return new PubDto()
				.id(pub.getId())
				.name(pub.getName())
				.description(pub.getDescription())
				.openTime(pub.getOpenTime().toString())
				.image(pub.getImage())
				.content(pub.getContentType());
	}

}
