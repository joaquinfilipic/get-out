package ar.edu.itba.paw.interfaces.service.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.pub.information.PubFood;

public interface PubFoodService extends ExistenceCheckable {

	public PubFood create(final Long pubId, final String food, final Double price);

	public boolean update(final Long id, final Long pubId, final String food, final Double price);

	public Optional<PubFood> findById(final Long id);

	public List<PubFood> listFoodByPub(final Long pubId);

	public void delete(final Long id);

	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}
