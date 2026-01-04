package cn.wurock.blog.auth.filter;

import cn.wurock.blog.auth.service.JwtService;
import cn.wurock.blog.auth.service.DBUserDetailsSevice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static cn.wurock.blog.common.constant.URLConstant.WHITE_LIST;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final DBUserDetailsSevice dbUserDetailsService;
	private static final AntPathMatcher pathMatcher = new AntPathMatcher(); // 路径匹配器
	
	/**
	 * 拦截所有请求，进行 JWT 验证
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String servletPath = request.getServletPath();
		// 跳过白名单
		for (String path : WHITE_LIST) {
			if (pathMatcher.match(path, servletPath)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		
		String token = jwtService.getTokenFromRequest(request);
		// token不存在
		if (token == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Missing JWT token");
			return;
		}
		
		// 验证 token
		try {
			String username = jwtService.extractUsername(token);
			if (username == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid token: username is null");
				return;
			}
			
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails;
				try {
					userDetails = dbUserDetailsService.loadUserByUsername(username);
				} catch (UsernameNotFoundException e) {
					log.warn("用户 {} 不存在", username);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("User not found");
					return;
				}
				
				if (jwtService.isTokenValid(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					log.debug("JWT 认证成功: {}", username);
				} else {
					log.warn("JWT token 无效或已过期");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("Invalid or expired token");
					return;
				}
			}
		} catch (Exception e) {
			log.error("JWT 认证过程中发生异常", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authentication failed");
			return;
		}
		
		// 放行请求（继续执行后续过滤器）
		filterChain.doFilter(request, response);
	}
	
}
