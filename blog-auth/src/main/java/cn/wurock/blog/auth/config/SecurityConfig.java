package cn.wurock.blog.auth.config;

import cn.wurock.blog.auth.filter.JwtAuthenticationFilter;
import cn.wurock.blog.auth.service.DBUserDetailsSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static cn.wurock.blog.common.constant.URLConstant.WHITE_LIST;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // 启用 Spring Security 核心功能
@EnableMethodSecurity // 启用方法级权限控制（如 @PreAuthorize("hasRole('ADMIN')")）
@RequiredArgsConstructor // 注入依赖（自定义组件）
public class SecurityConfig {
	
	// 1. 注入自定义依赖（都是 Spring 容器中的 Bean）
	private final DBUserDetailsSevice dbUserDetailsService; // 数据库用户查询服务
	private final JwtAuthenticationFilter jwtAuthFilter; // JWT 认证拦截器（需自定义）
	private final JwtAuthenticationEntryPoint jwtAuthEntryPoint; // JWT 认证失败处理器（需自定义）
	private final LogoutHandler jwtLogoutHandler; // JWT 登出处理器（需自定义）
	
	/**
	 * 2. 注册 PasswordEncoder（密码编码器）
	 * 作用：Spring Security 用它验证前端传入的密码（与数据库中加密后的密码比对）
	 * 推荐 BCrypt 加密（不可逆，自带盐值）
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 3. 认证提供者（核心：绑定用户查询 + 密码校验逻辑）
	 * DaoAuthenticationProvider：Spring Security 提供的默认实现，适配 UserDetailsService
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(dbUserDetailsService); // 绑定自定义用户查询服务
		provider.setPasswordEncoder(passwordEncoder()); // 绑定密码编码器
		return provider;
	}
	
	/**
	 * 4. 认证管理器（供 LoginService 调用，触发认证流程）
	 * 从 AuthenticationConfiguration 中获取，自动关联上面的 AuthenticationProvider
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	/**
	 * 跨域处理配置
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// 允许的前端源（注意：必须和你前端实际地址完全一致！）
		configuration.setAllowedOrigins(Arrays.asList(
				"http://localhost",
				"http://120.24.179.231"
		));
		
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true); // 允许携带 Cookie / Authorization header
		
		// 如果你使用了自定义 header（如 Authorization），建议显式列出：
		// configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration); // 应用到所有 API 路径
		return source;
	}
	
	/**
	 * 5. 安全过滤器链（核心配置）
	 * 定义：接口权限规则、JWT 拦截、会话管理、异常处理等
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 前后端分离项目，关闭表单登录
				.formLogin(form -> form.disable())
				
				// 关闭 CSRF（前后端分离项目必关，否则跨域请求被拦截）
				.csrf(csrf -> csrf.disable())
				
				// 跨域配置
				.cors(withDefaults())
				
				// 认证失败处理器（JWT 无效/过期时返回统一错误）
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(jwtAuthEntryPoint) // 未认证/认证失败时触发
				)
				
				// 会话管理（前后端分离项目用无状态会话）
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 不创建 HTTP Session
				)
				
				// 接口权限控制规则（按顺序匹配，优先级：先细后粗）
				.authorizeHttpRequests(auth -> auth
						// 公开接口：无需登录即可访问
						.requestMatchers(WHITE_LIST).permitAll() // 登录/注册/文档/刷新 Token 接口
						// 管理员接口：仅 ADMIN 角色可访问（配合 @EnableMethodSecurity 也可在方法上控制）
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						// 所有其他请求：必须认证（登录后）才能访问
						.anyRequest().authenticated()
				)
				
				// 登出配置（前后端分离场景）
				.logout(logout -> logout
						.logoutUrl("/api/auth/logout") // 自定义登出接口
						.addLogoutHandler(jwtLogoutHandler) // 登出时删除 JWT Token（如黑名单）
						.logoutSuccessHandler((request, response, authentication) -> {
							SecurityContextHolder.clearContext(); // 清除安全上下文
						})
				)
				
				// 绑定认证提供者（让 Spring Security 使用我们的数据库用户认证逻辑）
				.authenticationProvider(authenticationProvider())
				
				// 添加 JWT 拦截器（在用户名密码认证过滤器之前执行）
				// 作用：请求到达时先校验 JWT Token，有效则直接放行，无效则触发认证失败
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
}
