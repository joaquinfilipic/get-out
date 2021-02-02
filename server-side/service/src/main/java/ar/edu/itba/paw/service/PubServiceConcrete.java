package ar.edu.itba.paw.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.PubDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.pub.Pub;

@Service
@Transactional
public class PubServiceConcrete implements PubService {

	@Autowired
	private PubDAO pubDAO;

	@Override
	public Pub create(final Long userId, final String name, final String description, 
			final LocalTime openTime, final Image image) {
		return pubDAO.create(userId, name, description, openTime, image);
	}

	@Override
	public boolean updatePub(final Long id, final String name, final String description, final LocalTime openTime) {
		final Optional<Pub> result = findById(id);
		final boolean updated = !result
				.filter(pub -> pub.getName().equals(name))
				.filter(pub -> pub.getDescription().equals(description))
				.filter(pub -> openTime.equals(openTime))
				.isPresent();
		result.ifPresent(pub -> {
			pub.setName(name);
			pub.setDescription(description);
			pub.setOpenTime(openTime);
			pubDAO.update(pub);
		});
		return updated;
	}

	@Override
	public boolean updateImage(final Long id, final Image image) {
		return findById(id)
				.map(pub -> {
					pub.setImage(image.getData());
					pub.setContentType(image.getContentType());
					pubDAO.update(pub);
					return true;
				})
				.orElse(false);
	}

	@Override
	public Optional<Pub> findById(final Long id) {
		return pubDAO.findById(id);
	}

	@Override
	public List<Pub> findPubs() {
		return pubDAO.findPubs();
	}

	@Override
	public List<Pub> findPubsByName(final String name) {
		return pubDAO.findPubs().parallelStream()
				.filter(pub -> pub.getName().matches("(?i:.*" + name + ".*)"))
				.collect(Collectors.toList());
	}
	
	@Override
	public void delete(final Long id) {
		pubDAO.delete(id);
	}

}
