package ar.edu.itba.paw.service.pubInformation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubDrinkDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.PubDrinkService;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubDrink;

@Service
@Transactional
public class PubDrinkServiceConcrete implements PubDrinkService {

	@Autowired
	private PubDrinkDAO pubDrinkDAO;

	@Autowired
	private PubService pubService;

	@Override
	public PubDrink create(final Long pubId, final String drink, final Double price) {
		return pubDrinkDAO.create(pubId, drink, price);
	}

	@Override
	public boolean update(final Long id, final Long pubId, final String drink, final Double price) {
		Optional<PubDrink> result = findById(id);
		final Optional<Pub> pub = pubService.findById(pubId);
		final boolean updated = !result
				.filter(pubDrink -> pubDrink.getPubId().equals(pubId))
				.filter(pubDrink -> pubDrink.getDrink().equals(drink))
				.filter(pubDrink -> pubDrink.getPrice().equals(price))
				.isPresent();
		if (updated && pub.isPresent()) {
			PubDrink pubDrink = result.get();
			pubDrink.setPub(pub.get());
			pubDrink.setDrink(drink);
			pubDrink.setPrice(price);
			pubDrinkDAO.update(pubDrink);
		}
		return updated;
	}

	@Override
	public Optional<PubDrink> findById(final Long id) {
		return pubDrinkDAO.findById(id);
	}

	@Override
	public List<PubDrink> listDrinksByPub(final Long pubId) {
		return pubDrinkDAO.listDrinksByPub(pubId);
	}

	@Override
	public void delete(final Long id) {
		pubDrinkDAO.delete(id);
	}

}
