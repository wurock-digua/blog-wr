package cn.wurock.blog.auth.service;

import cn.wurock.blog.auth.entity.CustomUserDetails;
import cn.wurock.blog.user.entity.User;
import cn.wurock.blog.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class DBUserDetailsSevice implements UserDetailsService {
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User registerUser = userMapper.findUserByUsername(username);
		if (registerUser == null) {
			throw new UsernameNotFoundException("该用户不存在");
		} else {
			Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
			return new CustomUserDetails(
					registerUser.getId(),
					registerUser.getUsername(),
					registerUser.getPassword(),
					authorities,
					true,
					true,
					true,
					true
			);
		}
	}
}
