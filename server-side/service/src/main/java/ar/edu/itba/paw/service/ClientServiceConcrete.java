package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.persistence.ClientDAO;
import ar.edu.itba.paw.interfaces.service.ClientService;
import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.profile.Gender;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceConcrete implements ClientService {

	@Autowired
	private ClientDAO clientDAO;

	@Override
	public boolean updateProfile(final Long id, final String bio, final Gender gender) {
		final Optional<Client> result = findById(id);
		final boolean updated = !result
				.filter(client -> client.getGender().equals(gender))
				.filter(client -> client.getBio().equals(bio))
				.isPresent();
		result.ifPresent(client -> {
			client.setBio(bio);
			client.setGender(gender);
			clientDAO.update(client);
		});
		return updated;
	}
	
	@Override
	public boolean updateAvatar(final Long id, final Image avatar) {
		return findById(id)
				.map(client -> {
					client.setAvatar(avatar.getData());
					client.setContentType(avatar.getContentType());
					clientDAO.update(client);
					return true;
				})
				.orElse(false);
	}
	
	@Override
	public Optional<Client> findById(final Long id) {
		return clientDAO.findById(id);
	}
	
}
