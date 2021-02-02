package ar.edu.itba.paw.service.pubInformation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.pubInformation.PubInfoDAO;
import ar.edu.itba.paw.interfaces.service.PubService;
import ar.edu.itba.paw.interfaces.service.pubInformation.PubInfoService;
import ar.edu.itba.paw.model.pub.Pub;
import ar.edu.itba.paw.model.pub.information.PubInfo;

@Service
@Transactional
public class PubInfoServiceConcrete implements PubInfoService {
	
	@Autowired
	private PubInfoDAO pubInfoDAO;
	
	@Autowired
	private PubService pubService;
	
	@Override
	public PubInfo create(final Long pubId, final String address, final Double price) {
		return pubInfoDAO.create(pubId, address, price);
	}

	@Override
	public boolean update(final Long id, final Long pubId, final String address, final Double price) {
		Optional<PubInfo> result = findById(id);
		final Optional<Pub> pub = pubService.findById(pubId);
		final boolean updated = !result
				.filter(pubInfo -> pubInfo.getPubId().equals(pubId))
				.filter(pubInfo -> pubInfo.getAddress().equals(address))
				.filter(pubInfo -> pubInfo.getPrice().equals(price))
				.isPresent();
		if (updated && pub.isPresent()) {
			PubInfo pubInfo = result.get();
			pubInfo.setPub(pub.get());
			pubInfo.setAddress(address);
			pubInfo.setPrice(price);
			pubInfoDAO.update(pubInfo);
		}
		return updated;
	}

	@Override
	public Optional<PubInfo> findById(final Long id) {
		return pubInfoDAO.findById(id);
	}
	
	@Override
	public Optional<PubInfo> findByPubId(final Long id) {
		return pubInfoDAO.findByPubId(id);
	}

	@Override
	public void delete(final Long id) {
		pubInfoDAO.delete(id);
	}

}
