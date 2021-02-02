package ar.edu.itba.paw.webapp.config;

import static org.springframework.http.HttpMethod.*;
import ar.edu.itba.paw.interfaces.config.SecurityProperties;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan({
	"ar.edu.itba.paw.service.auth"
})
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	protected static final String API_ENDPOINT_v1 = "/api/v1.0";

	@Inject protected UserDetailsService service;
	@Inject protected SecurityProperties config;
	@Inject protected JWTConfig jwtConfig;

	@Override
	protected void configure(final AuthenticationManagerBuilder builder)
			throws Exception {
		builder.authenticationProvider(daoAuthenticationProvider(config.getPasswordEncoder()));
	}

	@Override
	protected void configure(final HttpSecurity http)
			throws Exception {
		http.userDetailsService(service)
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.invalidSessionUrl("/")
			.and().authorizeRequests()
				.antMatchers(POST, api("/users"), api("/login")).anonymous()
				.antMatchers(GET, api("/user/*")).permitAll()
				.antMatchers(GET, api("/pub/*")).permitAll()
				.antMatchers(GET, api("/pubs/date/*")).permitAll()
				.antMatchers(GET, api("/attendance/pub/*/date/*")).permitAll()
				.antMatchers(GET, api("/coordinates/*")).permitAll()
				.antMatchers(GET, api("/coordinates/pub/*")).permitAll()				
				.antMatchers(GET, api("/pub/information/*")).permitAll()
				.antMatchers(GET, api("/pub/information/pub/*")).permitAll()
				.antMatchers(GET, api("/promos/*")).permitAll()
				.antMatchers(GET, api("/promos/pub/*")).permitAll()
				.antMatchers(GET, api("/drinks/*")).permitAll()
				.antMatchers(GET, api("/drinks/pub/*")).permitAll()
				.antMatchers(GET, api("/food/*")).permitAll()
				.antMatchers(GET, api("/food/pub/*")).permitAll()
				.anyRequest().authenticated()
			.and().csrf()
				.disable()
			.apply(jwtConfig);
	}

	@Override
	public void configure(final WebSecurity web) {
		web.ignoring()
			.antMatchers(GET, "/", "/index.html")
			.antMatchers(GET, "/components/**/*.css", "/css/**/*.css", "/lib/css/**/*.css")
			.antMatchers(GET, "/components/**/*.html", "/errors/**/*.html")
			.antMatchers(GET, "/amd/**/*.js", "/components/**/*.js", "/directives/**/*.js")
			.antMatchers(GET, "/i18n/**/*.js", "/js/**/*.js", "/lib/**/*.js", "/services/**/*.js")
			.antMatchers(GET, "/images/**/*.gif", "/images/**/*.ico", "/images/**/*.jpg", "/images/**/*.png");
	}

	@Override @Bean
	public AuthenticationManager authenticationManagerBean()
			throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(final PasswordEncoder encoder) {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(encoder);
		return provider;
	}

	protected static String api(final String path) {
		return API_ENDPOINT_v1 + path;
	}
}
