package com.dds.ssjh.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dds.ssjh.model.User;
import com.dds.ssjh.service.UserService;

@Service
public class DdsUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		User user = userService.loadByName(username);
		if (user != null) {
			DdsUser ddsUser = new DdsUser(authorities, user.getName(), user.getPassword(), false, false, false, true);
			return ddsUser;
		} else {
			throw new UsernameNotFoundException("Username or password is invalid!");
		}
	}

}
