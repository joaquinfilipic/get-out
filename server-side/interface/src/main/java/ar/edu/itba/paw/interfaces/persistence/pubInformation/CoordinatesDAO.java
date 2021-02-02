package ar.edu.itba.paw.interfaces.persistence.pubInformation;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.pub.information.Coordinates;

public interface CoordinatesDAO extends Mergeable<Coordinates> {

	public Coordinates create(final Long pubId, final Double latitude, final Double longitude);

	public Optional<Coordinates> findById(final Long id);
	
	public Optional<Coordinates> findByPubId(final Long id);

	public void delete(final Long id);

}
