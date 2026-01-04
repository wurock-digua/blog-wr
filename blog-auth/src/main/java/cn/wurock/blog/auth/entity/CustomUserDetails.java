package cn.wurock.blog.auth.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;
	private final Long userId;
	private final String username;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities; // 权限列表
	private final boolean enabled; // 账户是否启用
	private final boolean accountNonExpired; // 账户是否未过期
	private final boolean accountNonLocked; // 账户是否未锁定
	private final boolean credentialsNonExpired; // 密码是否未过期
	
	public Long getUserId() {
		return userId;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
}
