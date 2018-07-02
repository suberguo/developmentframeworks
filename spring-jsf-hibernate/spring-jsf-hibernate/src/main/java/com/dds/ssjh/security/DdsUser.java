package com.dds.ssjh.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class DdsUser implements UserDetails {
	private final Collection<? extends GrantedAuthority> authorities;
	private final String password;
	private final String username;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;

	public DdsUser(Collection<? extends GrantedAuthority> authorities, String username, String password,
			boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
		super();
		this.authorities = authorities;
		this.password = password;
		this.username = username;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
