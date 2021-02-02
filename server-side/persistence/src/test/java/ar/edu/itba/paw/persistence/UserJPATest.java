package ar.edu.itba.paw.persistence;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import ar.edu.itba.paw.interfaces.persistence.UserDAO;
import ar.edu.itba.paw.interfaces.persistence.relation.AttendanceDAO;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.model.relation.Attendance;
import ar.edu.itba.paw.persistence.support.PersistenceTestConfig;
import ar.edu.itba.paw.persistence.support.TestDataFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Sql("classpath:test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class UserJPATest {

	private static final String[] USERNAME = { "master", "dobreva" };
	private static final String[] PASSWORD = { "master:password", "getout.nina.1.9.1989" };
	private static final String[] NAME = { "Master Tester", "Nikolina Konstantinova Dobreva" };
	private static final String[] MAIL = { "master@getout.ar", "dobreva@itba.edu.ar" };

	private User[] users;

	@Autowired
	private TestDataFactory factory;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AttendanceDAO attendanceDAO;

	@Before
	public void setUp() throws Exception {
		factory.cleanUsers();
		users = factory.createUserArray("UserJPATest", 3);
	}

	/**
	 * <p>
	 * Se espera que el DAO permita crear un usuario, y que respete para ello las
	 * propiedades especificadas del mismo, independientemente de la forma en la
	 * cual se persista el estado. Para parámetros válidos, la creación nunca
	 * debería fallar.
	 * </p>
	 */

	@Test
	public void canCreateAUser() {
		final User user = userDAO.create(USERNAME[1], PASSWORD[1], NAME[1], MAIL[1]);
		assertThat(user, hasProperty("username", equalTo(USERNAME[1])));
		assertThat(user, hasProperty("password", equalTo(PASSWORD[1])));
		assertThat(user, hasProperty("name", equalTo(NAME[1])));
		assertThat(user, hasProperty("mail", equalTo(MAIL[1])));
		assertThat("La cantidad de tuplas es inválida", factory.rowsIn("Users"), is(users.length + 1));
	}

	/**
	 * <p>
	 * Los usuarios deberían tener identidad única, y por ello debería ser posible
	 * acceder a sus propiedades bajo un identificador único en todo momento,
	 * durante la vida útil del mismo (<i>i.e.</i>, antes de borrarlo).
	 * </p>
	 */

	@Test
	public void userHasIdentity() {
		final Optional<User> result = userDAO.findById(users[0].getId());
		assertTrue("El usuario no existe en la base de datos", result.isPresent());
		final User user = result.get();
		assertThat(user, samePropertyValuesAs(users[0]));
	}

	/**
	 * <p>
	 * El nombre de usuario debe ser único, por lo cual la creación de otro usuario
	 * con el mismo nombre debería violar este esquema.
	 * </p>
	 */

	@Test(expected = PersistenceException.class)
	public void usernameIsUnique() {
		userDAO.create(users[0].getUsername(), "tester:password", "Tester", "tester@itba.edu.ar");
	}

	/**
	 * <p>
	 * Al igual que el nombre de usuario, el e-mail debería estar asociado a una
	 * única cuenta y no debería repetirse.
	 * </p>
	 */

	@Test(expected = PersistenceException.class)
	public void mailIsUnique() {
		userDAO.create("tester", "tester:password", "Tester", users[0].getMail());
	}

	/**
	 * <p>
	 * Un usuario recién creado no debería tener asistencia a ningún evento, lo que
	 * es equivalente a decir que no se registra al mismo de forma automática, al
	 * momento de crearlo.
	 * </p>
	 */

	@Test
	public void newUserHasNoAttendance() {
		final List<Attendance> attendance = attendanceDAO.listAttendanceByUser(users[0].getId());
		assertThat("El usuario está anotado en algún evento de forma automática", attendance, is(empty()));
	}

	@Test
	public void findByKeyIsSemanticallyEquivalentTofindByUsername() {
		final Optional<User> result = userDAO.findByKey("username", users[0].getUsername());
		assertTrue("El usuario no existe en la base de datos", result.isPresent());
		final User user = result.get();
		assertThat(user, samePropertyValuesAs(users[0]));
	}

	@Test
	public void findByKeyIsSemanticallyEquivalentTofindByID() {
		final Optional<User> result = userDAO.findByKey("id", users[0].getId());
		assertTrue("El usuario no existe en la base de datos", result.isPresent());
		final User user = result.get();
		assertThat(user, samePropertyValuesAs(users[0]));
	}
}
