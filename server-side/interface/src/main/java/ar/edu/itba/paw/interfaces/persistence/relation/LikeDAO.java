package ar.edu.itba.paw.interfaces.persistence.relation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.relation.Likes;

public interface LikeDAO {
	
	public Likes create(final Long senderId, final Long receiverId, final Long pubId, final LocalDate date);

	public Optional<Likes> findById(final Long id);
	
	public Optional<Likes> findBySenderAndReceiverAndPubAndDate(final Long senderId, final Long receiverId, 
			final Long pubId, final LocalDate date);
	
	public List<Likes> listBySenderAndPubAndDate(final Long senderId, final Long pubId, final LocalDate date);
	
	public List<Likes> listByReceiverAndPubAndDate(final Long receiverId, final Long pubId, final LocalDate date);
	
	public List<Likes> listBySender(final Long id);
	
	public List<Likes> listByReceiver(final Long id);
	
	public void delete(final Long id);
	
}
