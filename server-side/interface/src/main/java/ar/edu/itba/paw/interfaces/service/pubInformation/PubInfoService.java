package ar.edu.itba.paw.interfaces.service.pubInformation;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.pub.information.PubInfo;

public interface PubInfoService extends ExistenceCheckable {
	
	public PubInfo create(final Long pubId, final String address, final Double price);

	public boolean update(final Long id, final Long pubId, final String address, final Double price);

	public Optional<PubInfo> findById(final Long id);
	
	public Optional<PubInfo> findByPubId(final Long id);

	public void delete(final Long id);
	
	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}
