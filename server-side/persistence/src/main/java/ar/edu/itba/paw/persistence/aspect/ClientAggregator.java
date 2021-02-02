package ar.edu.itba.paw.persistence.aspect;

import ar.edu.itba.paw.interfaces.functional.Aggregator;
import ar.edu.itba.paw.model.profile.Client;
import ar.edu.itba.paw.model.profile.Gender;
import ar.edu.itba.paw.model.profile.User;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Repository;

@Aspect
@Repository
public class ClientAggregator implements Aggregator<Client> {

	@PersistenceContext
	protected EntityManager manager;

	@Before("execution(* ar.edu.itba.paw.interfaces.persistence.ClientDAO.*(..)) && args(id,..)")
	public void deployProfile(final JoinPoint join, final Long id) {
		aggregate(id);
	}

	@Override
	public Optional<Client> aggregate(final Long id) {
		return Optional.ofNullable(manager.find(User.class, id))
				.map(user -> Optional.ofNullable(manager.find(Client.class, id))
						.orElseGet(() -> {
							final Client client = new Client.Builder()
									.user(user)
									.gender(Gender.OTHER)
									.bio("")
									.build();
							manager.persist(client);
							manager.flush();
							return client;
						}));
	}
}
