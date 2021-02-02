package ar.edu.itba.paw.interfaces.service;

import java.time.LocalDate;
import java.util.List;

import ar.edu.itba.paw.model.profile.Client;

public interface SocialService {

	public void like(final Long userIdFrom,
			final Long userIdTo,
			final Long pubId,
			final LocalDate date);
	
	public void cancelLike(final Long userIdFrom,
			final Long userIdTo,
			final Long pubId,
			final LocalDate date);
	
	public void reject(final Long userIdFrom,
			final Long userIdTo,
			final Long pubId,
			final LocalDate date);
	
	public void cancelReject(final Long userIdFrom,
			final Long userIdTo,
			final Long pubId,
			final LocalDate date);
	
	public List<Client> getPending(final Long userId,
			final Long pubId,
			final LocalDate date);
	
	public List<Client> getCandidates(final Long userId,
			final Long pubId,
			final LocalDate date);
	
	public List<Client> getMatches(final Long userId,
			final Long pubId,
			final LocalDate date);
	
}
