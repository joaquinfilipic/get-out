package ar.edu.itba.paw.interfaces.persistence.pubInformation;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.pub.information.PubInfo;

public interface PubInfoDAO extends Mergeable<PubInfo> {

	public PubInfo create(final Long pubId, final String address, final Double price);

	public Optional<PubInfo> findById(final Long id);
	
	public Optional<PubInfo> findByPubId(final Long id);

	public void delete(final Long id);

}
