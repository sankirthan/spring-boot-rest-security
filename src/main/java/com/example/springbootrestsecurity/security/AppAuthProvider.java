package com.example.springbootrestsecurity.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.springbootrestsecurity.jpa.entity.User;
import com.example.springbootrestsecurity.jpa.repository.UserRepository;

/**
 * Provides a custom <code>AuthenticationProvider</code> that authenticates users 
 * against a custom database table, as well as provides locking capability if
 * number of consecutive failed login attempts reaches a limit. This is a must 
 * to avoid brute force login attack.
 * 
 * Uses the <code>BCryptPasswordEncoder</code> for comparing encoded passwords
 * in database.
 * 
 * @author Sankirthan Thayaparan
 */
@Component
public class AppAuthProvider implements AuthenticationProvider {
	
	private static final int MAXIMUM_FAILED_LOGIN_ATTEMPTS = 10;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = (String) auth.getPrincipal();
		String password = (String) auth.getCredentials();
		
		Optional<User> userOptional = userRepository.findById(username);
		if(!userOptional.isPresent()) {
			throw new UsernameNotFoundException(username + " not found");
		}
		
		User user = userOptional.get();
		
		if(!user.isActive()) {
			throw new DisabledException(username + " is not active");
		}
		
		if(user.isLocked()) {
			throw new LockedException(username + " is locked");
		}
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			if(user.getFailedLoginCount() + 1 >= MAXIMUM_FAILED_LOGIN_ATTEMPTS) {
				userRepository.incrementFailedLoginCountAndLock(username);
				throw new LockedException(username + " is locked");
			}
			else {
				userRepository.incrementFailedLoginCount(username);
				throw new BadCredentialsException("password or username is incorrect");
			}
		}
		
		if(user.getFailedLoginCount() != 0) {
			userRepository.unlock(username);
		}
		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
										.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
										.collect(Collectors.toList());
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
	
}
