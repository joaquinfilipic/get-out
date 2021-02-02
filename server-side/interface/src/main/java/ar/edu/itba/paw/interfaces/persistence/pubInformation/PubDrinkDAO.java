package ar.edu.itba.paw.interfaces.persistence.pubInformation;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.pub.information.PubDrink;

public interface PubDrinkDAO extends Mergeable<PubDrink> {

	public PubDrink create(final Long pubId, final String drink, final Double price);
	
	public Optional<PubDrink> findById(final Long id);
	
	public List<PubDrink> listDrinksByPub(final Long pubId);
	
	public void delete(final Long id);
	
}
