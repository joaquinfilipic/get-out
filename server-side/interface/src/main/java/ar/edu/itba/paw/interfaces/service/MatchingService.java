package ar.edu.itba.paw.interfaces.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.relation.Likes;
import ar.edu.itba.paw.model.relation.Reject;

public interface MatchingService {

	public Likes createLike(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);
	
	public Optional<Likes> findLike(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);
	
	public void deleteLike(final Long id);
	
	// +---------------------------------------------------------------------+
	
	public Reject createReject(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);
	
	public Optional<Reject> findReject(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);
	
	public void deleteReject(final Long id);
	
	// +---------------------------------------------------------------------+
		
	public List<Client> getPendings(final Long userId, final Long pubId, final LocalDate date);
	
	public List<Client> getCandidates(final Long userId, final Long pubId, final LocalDate date);
	
	public List<Client> getMatches(final Long userId, final Long pubId, final LocalDate date);
		
}
