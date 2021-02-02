package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.image.Image;
import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.profile.Gender;
import java.util.Optional;

public interface ClientService {
	
	public boolean updateProfile(final Long id, final String bio, final Gender gender);
	
	public boolean updateAvatar(final Long id, final Image image);
	
	public Optional<Client> findById(final Long id);
	
}
