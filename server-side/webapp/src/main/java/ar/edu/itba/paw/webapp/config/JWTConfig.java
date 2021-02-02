package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.auth.JWTAuthenticationService;
import ar.edu.itba.paw.webapp.filter.AccessDeniedFilter;
import ar.edu.itba.paw.webapp.filter.JWTAuthenticationFilter;
import javax.inject.Inject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@ComponentScan({
	"ar.edu.itba.paw.service.auth"
})
@Configuration
public class JWTConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Inject protected JWTAuthenticationService jwtService;

	@Override
	public void configure(final HttpSecurity http)
			throws Exception {
		http.addFilterBefore(
					new JWTAuthenticationFilter(jwtService),
					UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(
					new AccessDeniedFilter(),
					ExceptionTranslationFilter.class);
	}
}
