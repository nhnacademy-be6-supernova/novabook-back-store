package store.novabook.store.common.security.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import store.novabook.store.common.security.entity.Members;

public class CustomUserDetails implements UserDetails {
	private final transient Members members;

	public CustomUserDetails(Members members) {
		this.members = members;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add((GrantedAuthority)members::getRole);

		return collection;
	}

	public long getId() {
		return members.getId();
	}

	@Override
	public String getPassword() {
		return members.getPassword();
	}

	@Override
	public String getUsername() {
		return Long.toString(members.getId());
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
		return true;
	}

	public Object getDetails() {
		return members;
	}
}
