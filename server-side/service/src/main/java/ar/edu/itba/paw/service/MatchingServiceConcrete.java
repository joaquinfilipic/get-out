package ar.edu.itba.paw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.ClientDAO;
import ar.edu.itba.paw.interfaces.persistence.relation.LikeDAO;
import ar.edu.itba.paw.interfaces.persistence.relation.RejectDAO;
import ar.edu.itba.paw.interfaces.service.MatchingService;
import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.relation.Likes;
import ar.edu.itba.paw.model.relation.Reject;

@Service
@Transactional
public class MatchingServiceConcrete implements MatchingService {

	@Autowired
	private LikeDAO likeDAO;

	@Autowired
	private RejectDAO rejectDAO;
	
	@Autowired
	private ClientDAO clientDAO;

	@Override
	public Likes createLike(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		return likeDAO.create(senderId, receiverId, pubId, date);
	}
	
	@Override
	public Optional<Likes> findLike(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		return likeDAO.findBySenderAndReceiverAndPubAndDate(senderId, receiverId, pubId, date);
	}
	
	@Override
	public void deleteLike(final Long id) {
		likeDAO.delete(id);
	}
	
	// +---------------------------------------------------------------------+

	@Override
	public Reject createReject(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		return rejectDAO.create(senderId, receiverId, pubId, date);
	}

	@Override
	public Optional<Reject> findReject(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date) {
		return rejectDAO.findBySenderAndReceiverAndPubAndDate(senderId, receiverId, pubId, date);
	}

	@Override
	public void deleteReject(final Long id) {
		rejectDAO.delete(id);
	}
	
	// +---------------------------------------------------------------------+

	@Override
	public List<Client> getPendings(final Long userId, final Long pubId, final LocalDate date) {
		return clientDAO.getPendingByPubAndDate(userId, pubId, date);
	}
	
	@Override
	public List<Client> getCandidates(final Long userId, final Long pubId, final LocalDate date) {
		return clientDAO.getCandidatesByPubAndDate(userId, pubId, date);
	}
	
	@Override
	public List<Client> getMatches(final Long userId, final Long pubId, final LocalDate date) {
		return clientDAO.getMatchesByPubAndDate(userId, pubId, date);
	}

}
