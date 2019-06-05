package com.example.springbootrestsecurity.controller;

import io.swagger.annotations.Api;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.springbootrestsecurity.security.AppAuthProvider;

@Api
@RestController
public class LoginController {

	@Autowired
	private AppAuthProvider appAuthenticationProvider;
	
	@PostMapping("/login")
	public String login(@RequestParam("username") String username, 
										@RequestParam("password") String password) {
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username and password is required");
		}
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			appAuthenticationProvider.authenticate(authentication);
			/*
				Here we just return base64 encoded username:password
				to be used in http header Authorization: Bearer <token>.
				This type of authentication must only be done with 
				http-secured (https). 
				
				This type of token scheme is more appropriate 
				when you want account changes (i.e. password change, role change, status, etc.) 
				enforced immediately. Because the AuthorizationTokenDecodeFilter will 
				always decode the token and authenticate against database for every 
				request.
			*/
			return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password does not match");
		}
		
	}
	
}
