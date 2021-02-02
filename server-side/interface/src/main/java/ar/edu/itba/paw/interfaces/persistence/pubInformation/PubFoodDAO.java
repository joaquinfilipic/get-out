package ar.edu.itba.paw.interfaces.persistence.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.pub.information.PubFood;

public interface PubFoodDAO extends Mergeable<PubFood> {

	public PubFood create(final Long pubId, final String food, final Double price);
	
	public Optional<PubFood> findById(final Long id);
	
	public List<PubFood> listFoodByPub(final Long pubId);
	
	public void delete(final Long id);
	
}
