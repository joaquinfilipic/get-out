package ar.edu.itba.paw.interfaces.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.interfaces.functional.ExistenceCheckable;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.pub.Pub;

public interface PubService extends ExistenceCheckable {

	public Pub create(final Long userId, final String name, final String description,
			final LocalTime openTime, final Image image);

	public boolean updatePub(final Long id, final String name, final String description, final LocalTime openTime);

	public boolean updateImage(final Long id, final Image image);

	public Optional<Pub> findById(final Long id);

	public List<Pub> findPubs();

	public List<Pub> findPubsByName(final String name);

	public void delete(final Long id);

	public default boolean exists(final Long id) {
		return findById(id).isPresent();
	}

}
