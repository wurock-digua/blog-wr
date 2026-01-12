package cn.wurock.blog.web.controller;

import cn.wurock.blog.auth.dto.LoginRequest;
import cn.wurock.blog.auth.dto.RegisterRequest;
import cn.wurock.blog.auth.service.AuthService;
import cn.wurock.blog.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	/**
	 * 用户登录
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/login")
	public Result<String> login(@Validated @RequestBody LoginRequest loginRequest) {
		String token = authService.login(loginRequest);
		return Result.success(token);
	}
	
	/**
	 * 新用户注册
	 * @param registerRequest
	 * @return
	 */
	@PostMapping("/register")
	public Result register(@Validated @RequestBody RegisterRequest registerRequest) {
		authService.register(registerRequest);
		return Result.success();
	}
}
