package ar.edu.itba.paw.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.profile.User;
import ar.edu.itba.paw.persistence.support.PersistenceTestConfig;
import ar.edu.itba.paw.persistence.support.TestDataFactory;
import ar.edu.itba.paw.service.support.ServiceTestConfig;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Sql("classpath:test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
	PersistenceTestConfig.class,
	ServiceTestConfig.class
})
public class UserServiceConcreteTest {

	private static final String USERNAME = "dobreva";
	private static final String PASSWORD = "getout dobreva ITBA.EDU.AR";
	private static final String NAME = "Nikolina Konstantinova Dobreva";
	private static final String MAIL = "dobreva@itba.edu.ar";

	@Inject
	private TestDataFactory factory;

	@Inject
	private UserService service;

	@Before
	public void before() throws Exception {
		factory.cleanUsers();
	}

	@Test
	public void passwordsAreNotPlain() {
		final User result = service.create(USERNAME, PASSWORD, NAME, MAIL);
		final List<String> words = Arrays.asList(PASSWORD.split(" "));
		for (final String word : words)
			assertThat(result.getPassword(), not(containsString(word)));
	}

	@Test
	public void usernamesShouldBeCanonical() {
		final User result = service.create(" 	A-UsEr-ThaT-is-NOT-CANOnical    ", PASSWORD, NAME, MAIL);
		assertThat(result.getUsername(), is(equalTo("a-user-that-is-not-canonical")));
	}

	@Test
	public void mailsShouldBeCanonical() {
		final User result = service.create(
				USERNAME, PASSWORD, NAME, " 		noN.CANONICAL.eMaIl@doMAin.Com    ");
		assertThat(result.getMail(), is(equalTo("non.canonical.email@domain.com")));
	}

	@Test
	public void namesAreTrimmed() {
		final User result = service.create(
				USERNAME, PASSWORD, "   Nina	   	Dobrev  ", MAIL);
		assertThat(result.getName(), is(equalTo("Nina	   	Dobrev")));
	}
}
