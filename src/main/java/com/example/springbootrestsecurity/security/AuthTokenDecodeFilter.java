package com.example.springbootrestsecurity.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter to decode the http header Authorization: Bearer <username:password>, and
 * create spring security <code>Authentication</code> token.
 * 
 * @author Sankirthan Thayaparan
 *
 */
@Component
public class AuthTokenDecodeFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		if(isAuthenticationRequired()) {
			Enumeration<String> authorizations = ((HttpServletRequest) request).getHeaders("Authorization");
			while(authorizations.hasMoreElements()) {
				String authorization = authorizations.nextElement();
				if(authorization != null) {
					String authorizationValue = authorization.trim();
					if(authorizationValue.toLowerCase().startsWith("bearer")) {
						String tokenValue = authorizationValue.substring(6).trim();
						String tokenValueDecoded = new String(Base64.getDecoder().decode(tokenValue));
						String[] tuple = tokenValueDecoded.split(":", 2);
						if(tuple.length == 2) {
							UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tuple[0], tuple[1]);
							SecurityContextHolder.getContext().setAuthentication(token);
							break;
						}
					}
				}
			}
			
		}
		
		chain.doFilter(request, response);
	}
	
	private boolean isAuthenticationRequired() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if ((authentication == null) || !authentication.isAuthenticated()) {
	        return true;
	    }
	    return false;
	}
	
}
