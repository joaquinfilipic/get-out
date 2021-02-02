package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.functional.Mergeable;
import ar.edu.itba.paw.model.profile.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientDAO extends Mergeable<Client> {
	
	public Optional<Client> findById(final Long id);
		
	public List<Client> getPendingByPubAndDate(final Long userId, final Long pubId, final LocalDate date);
	
	public List<Client> getCandidatesByPubAndDate(final Long userId, final Long pubId, final LocalDate date);
	
	public List<Client> getMatchesByPubAndDate(final Long userId, final Long pubId, final LocalDate date);
	
}
