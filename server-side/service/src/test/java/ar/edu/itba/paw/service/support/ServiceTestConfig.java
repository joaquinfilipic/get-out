package ar.edu.itba.paw.service.support;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import ar.edu.itba.paw.interfaces.config.SecurityProperties;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan({"ar.edu.itba.paw.service"})
@Configuration
@EnableWebSecurity
public class ServiceTestConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean @Primary
	public SecurityProperties securityProperties() {
		return new SecurityProperties() {

			@Override
			public int getTokenValidityInSeconds() {
				return 3600;
			}

			@Override
			public String getRememberKey() {
				return "totally-unsecure-remember-me-key";
			}

			@Override
			public PasswordEncoder getPasswordEncoder() {
				return encoder();
			}

			@Override
			public byte [] getJWTSecret() {
				return new byte [] {'f', 'u', 'c', 'k'};
			}

			@Override
			public long getJWTExpirationTimeInMilliseconds() {
				return 3600000;
			}
		};
	}

	// Extracted as is, from "ar.edu.itba.paw.webapp.SecurityConfig"
	protected static final String API_ENDPOINT_v1 = "/api/v1.0";

	@Inject protected UserDetailsService service;
	@Inject protected SecurityProperties config;
	//@Inject protected JWTConfig jwtConfig;

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
				.antMatchers(GET, "/", api("/user/*")).permitAll()
				.antMatchers(GET, api("/testing")).permitAll()
				.antMatchers(POST, api("/users"), api("/login")).anonymous()
				.anyRequest().authenticated()
			.and().csrf()
				.disable();
			//.apply(jwtConfig);
	}

	@Override
	public void configure(final WebSecurity web) {
		web.ignoring()
			.antMatchers(GET, "/components/**/*.css", "/css/**/*.css", "/lib/css/**/*.css")
			.antMatchers(GET, "/components/**/*.html", "/errors/**/*.html")
			.antMatchers(GET, "/amd/**/*.js", "/components/**/*.js", "/js/**/*.js", "/lib/**/*.js")
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
