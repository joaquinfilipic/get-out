package ar.edu.itba.paw.webapp.validator;

import java.util.Optional;

import ar.edu.itba.paw.model.error.ErrorType;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.support.GetOutException;

public class PubOwnerValidator {
	
	public static void validateOwnership(final Long userId, final Optional<Pub> pubOp) throws GetOutException {
		pubOp.ifPresent(pub -> {
			if (!pub.getUser().getId().equals(userId)) {
				throw GetOutException.of(ErrorType.BELONGS_TO_OTHER);
			}
		});
	}

}
