package com.website.eocs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.website.eocs.entity.Account;

public class AccountDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private final Account account;

	    public AccountDetails(Account account) {
	        this.account = account;
	    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(account.getRole() == 1) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if(account.getRole() == 2) {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return account.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if(account.getStatus() == "Inactive") {
			return false;
		}
		return true;
	}
	
}
