package cn.wurock.blog.auth.service;

import cn.wurock.blog.auth.dto.LoginRequest;
import cn.wurock.blog.auth.dto.RegisterRequest;
import cn.wurock.blog.user.dto.UserDTO;
import cn.wurock.blog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserService userService;
	
	/**
	 * 用户登录
	 * @param loginRequest
	 * @return
	 */
	@Transactional
	public String login(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
	
		// 1. 创建未认证的token
		Authentication authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		// 2. 触发 Spring Security 认证（会调用 UserDetailsService + PasswordEncoder）
		Authentication authentication = authenticationManager.authenticate(authToken);
		
		// 3. 获取认证后的用户详情
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		// 4. 生成 JWT Token
		String token = jwtService.generateToken(userDetails);
		
		// 5. （可选）更新用户最后登录时间
		
		return token;
	}
	
	/**
	 * 新用户注册
	 * @param registerRequest
	 */
	public void register(RegisterRequest registerRequest) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(registerRequest,userDTO);
		userService.register(userDTO);
	}
}
