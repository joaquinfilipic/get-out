package ar.edu.itba.paw.persistence.support;

import ar.edu.itba.paw.interfaces.persistence.UserDAO;
import ar.edu.itba.paw.model.image.ContentType;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.pub.Pub;
import java.time.LocalTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

	/**
	* <p>Permite obtener y desplegar objetos de prueba para realizar testing,
	* bajo JPA. Posee algunas funciones de utilidad adicionales.</p>
	*/

@Transactional
public class TestDataFactory {

	private final JdbcTemplate template;

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	public TestDataFactory(final @Qualifier("realtime") DataSource source) {
		template = new JdbcTemplate(source);
	}

	public static void sleep(final long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch (final InterruptedException spurious) {
		}
	}

	public void clean(final String table) {
		JdbcTestUtils.deleteFromTables(template, table);
	}

	public int rowsIn(final String table) {
		return JdbcTestUtils.countRowsInTable(template, table);
	}

	public void cleanUsers() {
		clean("Users");
	}

	public void cleanPubs() {
		clean("Pub");
	}

	public User createUser(final String key) {
		return userDAO.create("username-" + key, "622hgr2736rgFigH827gaAsd" + key, "Name " + key,
				"mail." + key + "@getout.ar");
	}

	public Pub createPub(final String key, final User user) {
		final long hour = Math.round(Math.random() * 23);
		final long minute = Math.round(Math.random() * 59);
		final String time = ((hour < 10) ? ("0" + hour) : hour)
			+ ":" + ((minute < 10) ? ("0" + minute) : minute);

		final Pub pub = new Pub.Builder()
				.user(user)
				.name("Pub Name " + key)
				.description("Description of pub " + key + ".")
				.openTime(LocalTime.parse(time))
				.image(key.getBytes())
				.content(ContentType.IMAGE_JPEG).build();

		manager.persist(pub);
		manager.flush();
		return pub;
	}

	public User[] createUserArray(final String key, final int size) {
		final User[] users = new User[size];
		for (int i = 0; i < size; ++i) {
			users[i] = createUser(key + i);
		}
		return users;
	}

	public Pub[] createPubArray(final String key, final User[] users) {
		final Pub[] pubs = new Pub[users.length];
		for (int i = 0; i < users.length; ++i) {
			pubs[i] = createPub(key + i, users[i]);
		}
		return pubs;
	}
}
