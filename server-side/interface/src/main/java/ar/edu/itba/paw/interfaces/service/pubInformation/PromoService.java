package ar.edu.itba.paw.interfaces.service.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.pub.information.Promo;

public interface PromoService extends ExistenceCheckable {
	
	public Promo create(final Long pubId, final String name, final String description);

	public boolean update(final Long id, final Long pubId, final String name, final String description);
	
	public Optional<Promo> findById(final Long id);
	
	public List<Promo> listPromosByPub(final Long pubId);
	
	public void delete(final Long id);
	
	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}
