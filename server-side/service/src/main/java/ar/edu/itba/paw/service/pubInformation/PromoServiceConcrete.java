package ar.edu.itba.paw.service.pubInformation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PromoDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.PromoService;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.Promo;

@Service
@Transactional
public class PromoServiceConcrete implements PromoService {

	@Autowired
	private PromoDAO promoDAO;

	@Autowired
	private PubService pubService;

	@Override
	public Promo create(final Long pubId, final String name, final String description) {
		return promoDAO.create(pubId, name, description);
	}

	@Override
	public boolean update(final Long id, final Long pubId, final String name, final String description) {
		Optional<Promo> result = findById(id);
		final Optional<Pub> pub = pubService.findById(pubId);
		final boolean updated = !result
				.filter(promo -> promo.getPubId().equals(pubId))
				.filter(promo -> promo.getName().equals(name))
				.filter(promo -> promo.getDescription().equals(description))
				.isPresent();
		if (updated && pub.isPresent()) {
			Promo promo = result.get();
			promo.setPub(pub.get());
			promo.setName(name);
			promo.setDescription(description);
			promoDAO.update(promo);
		}
		return updated;
	}

	@Override
	public Optional<Promo> findById(final Long id) {
		return promoDAO.findById(id);
	}

	@Override
	public List<Promo> listPromosByPub(final Long pubId) {
		return promoDAO.listPromosByPub(pubId);
	}

	@Override
	public void delete(final Long id) {
		promoDAO.delete(id);
	}

}
