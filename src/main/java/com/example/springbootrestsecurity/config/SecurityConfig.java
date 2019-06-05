package com.example.springbootrestsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.springbootrestsecurity.security.AppAuthProvider;
import com.example.springbootrestsecurity.security.AuthTokenDecodeFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthTokenDecodeFilter authorizationTokenDecodeFilter;
	
	@Autowired
	private AppAuthProvider appAuthProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(authorizationTokenDecodeFilter, BasicAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()  // resources
				.antMatchers("/swagger*/**", "/v2/api-docs/**", "/", "/csrf").permitAll() // swagger-ui
				.antMatchers("/login").permitAll() // login
				.anyRequest().fullyAuthenticated()
				.and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().csrf().disable(); // because we use Authorization: Bearer <token>, which is impervious to csrf
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(appAuthProvider);
	}
	
}
