package ar.edu.itba.paw.service.pubInformation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubFoodDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.PubFoodService;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubFood;

@Service
@Transactional
public class PubFoodServiceConcrete implements PubFoodService {
	
	@Autowired
	private PubFoodDAO pubFoodDAO;
	
	@Autowired
	private PubService pubService;

	@Override
	public PubFood create(final Long pubId, final String food, final Double price) {
		return pubFoodDAO.create(pubId, food, price);
	}

	@Override
	public boolean update(final Long id, final Long pubId, final String food, final Double price) {
		Optional<PubFood> result = findById(id);
		final Optional<Pub> pub = pubService.findById(pubId);
		final boolean updated = !result
				.filter(pubFood -> pubFood.getPubId().equals(pubId))
				.filter(pubFood -> pubFood.getFood().equals(food))
				.filter(pubFood -> pubFood.getPrice().equals(price))
				.isPresent();
		if (updated && pub.isPresent()) {
			PubFood pubFood = result.get();
			pubFood.setPub(pub.get());
			pubFood.setFood(food);
			pubFood.setPrice(price);
			pubFoodDAO.update(pubFood);
		}
		return updated;
	}

	@Override
	public Optional<PubFood> findById(final Long id) {
		return pubFoodDAO.findById(id);
	}

	@Override
	public List<PubFood> listFoodByPub(final Long pubId) {
		return pubFoodDAO.listFoodByPub(pubId);
	}

	@Override
	public void delete(final Long id) {
		pubFoodDAO.delete(id);
	}

}
