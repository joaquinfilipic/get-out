package ar.edu.itba.paw.interfaces.service.pubInformation;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.pub.information.Coordinates;

public interface CoordinatesService extends ExistenceCheckable {

	public Coordinates create(final Long pubId, final Double latitude, final Double longitude);

	public boolean update(final Long id, final Long pubId, final Double latitude, final Double longitude);

	public Optional<Coordinates> findById(final Long id);
	
	public Optional<Coordinates> findByPubId(final Long id);

	public void delete(final Long id);

	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}