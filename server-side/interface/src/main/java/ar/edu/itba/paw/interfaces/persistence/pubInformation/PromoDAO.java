package ar.edu.itba.paw.interfaces.persistence.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.pub.information.Promo;

public interface PromoDAO extends Mergeable<Promo> {

	public Promo create(final Long pubId, final String name, final String description);
	
	public Optional<Promo> findById(final Long id);
		
	public List<Promo> listPromosByPub(final Long pubId);
	
	public void delete(final Long id);

}
