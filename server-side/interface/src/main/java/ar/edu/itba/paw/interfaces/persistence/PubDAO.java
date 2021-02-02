package ar.edu.itba.paw.interfaces.persistence;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.pub.Pub;

public interface PubDAO extends Mergeable<Pub> {

	public Pub create(final Long userId, final String name, final String description, 
			final LocalTime openTime, final Image image);

	public Optional<Pub> findById(final Long id);
	
	public List<Pub> findPubs();

	public void delete(final Long id);

}
