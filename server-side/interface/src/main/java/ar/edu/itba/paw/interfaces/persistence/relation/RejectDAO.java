package ar.edu.itba.paw.interfaces.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.relation.Reject;

public interface RejectDAO {

	public Reject create(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);

	public Optional<Reject> findById(final Long id);
	
	public Optional<Reject> findBySenderAndReceiverAndPubAndDate(final Long senderId, final Long receiverId, 
			final Long pubId, final LocalDate date);
	
	public List<Reject> listBySenderAndPubAndDate(final Long senderId, final Long pubId, final LocalDate date);
	
	public List<Reject> listByReceiverAndPubAndDate(final Long receiverId, final Long pubId, final LocalDate date);
	
	public List<Reject> listBySender(final Long id);
	
	public List<Reject> listByReceiver(final Long id);
	
	public void delete(final Long id);
	
}
