package com.dds.ssjh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class DdsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userService;

	public UserDetailsService getUserService() {
		return userService;
	}

	public void setUserService(UserDetailsService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails user = userService.loadUserByUsername(authentication.getName());
		if (!user.getPassword().equals(authentication.getCredentials())) {
			System.out.println(authentication.getName());
			System.out.println(authentication.getCredentials());
			System.out.println(authentication.getPrincipal());
			System.out.println(authentication.getAuthorities());
			throw new BadCredentialsException("Username or password is invalid!");
		}
		return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
