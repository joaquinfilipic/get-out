package ar.edu.itba.paw.service.pubInformation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.CoordinatesDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.CoordinatesService;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.Coordinates;

@Service
@Transactional
public class CoordinatesServiceConcrete implements CoordinatesService {

	@Autowired
	private CoordinatesDAO coordinatesDAO;
	
	@Autowired
	private PubService pubService;
	
	@Override
	public Coordinates create(final Long pubId, final Double latitude, final Double longitude) {
		return coordinatesDAO.create(pubId, latitude, longitude);
	}

	@Override
	public boolean update(final Long id, final Long pubId, final Double latitude, final Double longitude) {
		Optional<Coordinates> result = findById(id);
		final Optional<Pub> pub = pubService.findById(pubId);
		final boolean updated = !result
				.filter(coordinates -> coordinates.getPubId().equals(pubId))
				.filter(coordinates -> coordinates.getLatitude().equals(latitude))
				.filter(coordinates -> coordinates.getLongitude().equals(longitude))
				.isPresent();
		if (updated && pub.isPresent()) {
			Coordinates coordinates = result.get();
			coordinates.setPub(pub.get());
			coordinates.setLatitude(latitude);
			coordinates.setLongitude(longitude);
			coordinatesDAO.update(coordinates);
		}
		return updated;
	}

	@Override
	public Optional<Coordinates> findById(final Long id) {
		return coordinatesDAO.findById(id);
	}
	
	@Override
	public Optional<Coordinates> findByPubId(final Long id) {
		return coordinatesDAO.findByPubId(id);
	}

	@Override
	public void delete(final Long id) {
		coordinatesDAO.delete(id);
	}

	
	
}
