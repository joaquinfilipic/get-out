package ar.edu.itba.paw.interfaces.service.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.pub.information.PubDrink;

public interface PubDrinkService extends ExistenceCheckable {
	
	public PubDrink create(final Long pubId, final String drink, final Double price);

	public boolean update(final Long id, final Long pubId, final String drink, final Double price);
	
	public Optional<PubDrink> findById(final Long id);
	
	public List<PubDrink> listDrinksByPub(final Long pubId);
	
	public void delete(final Long id);
	
	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}
