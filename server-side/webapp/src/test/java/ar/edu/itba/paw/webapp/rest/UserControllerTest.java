package ar.edu.itba.paw.webapp.rest;

import static org.junit.Assert.*;
import ar.edu.itba.paw.persistence.support.PersistenceTestConfig;
import ar.edu.itba.paw.service.support.ServiceTestConfig;
import ar.edu.itba.paw.webapp.config.WebTestConfig;
import ar.edu.itba.paw.webapp.support.GetOutMediaType;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(classes = {
	PersistenceTestConfig.class,
	ServiceTestConfig.class,
	WebTestConfig.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:test.sql")
@Transactional
public class UserControllerTest {

	@Inject
	private WebTarget target;

    @Test
    public void anExampleTest() {
		final Response response = target.path("/v1.0/user/1").request().get();
		assertEquals("Content-type should be: ",
			GetOutMediaType.APPLICATION_GETOUT_v1_JSON,
			response.getHeaderString(HttpHeaders.CONTENT_TYPE));
	}
}
