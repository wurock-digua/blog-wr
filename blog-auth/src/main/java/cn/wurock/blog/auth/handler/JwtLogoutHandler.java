package cn.wurock.blog.auth.handler;

import cn.wurock.blog.auth.service.DBUserDetailsSevice;
import cn.wurock.blog.auth.service.JwtService;
import cn.wurock.blog.auth.service.TokenBlacklistService;
import cn.wurock.blog.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
	
	private final JwtService jwtService;
	private final TokenBlacklistService tokenBlacklistService;
	
	/**
	 * 处理登出逻辑（无Refresh Token）
	 * @param request
	 * @param response
	 * @param authentication
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 从请求中获取token
		String token = jwtService.getTokenFromRequest(request);
		if (token == null) {
			log.debug("No valid Bearer token found in request during logout");
			return;
		}
		
		
		// 解析token并获取jti
		Claims claims;
		try {
			claims = jwtService.parseToken(token);
		} catch (SignatureException e) {
			log.warn("Invalid JWT signature during logout", e);
			return;
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			log.debug("Expired JWT during logout, skipping blacklist", e);
			return;
		} catch (Exception e) {
			log.error("Failed to parse JWT during logout", e);
			return;
		}
		
		String jti = claims.getId();
		if (jti == null || jti.isEmpty()) {
			log.warn("JWT does not contain 'jti', cannot blacklist");
			return;
		}
		
		// 计算剩余有效期
		Date expiration = claims.getExpiration();
		long ttlMillis = expiration.getTime() - System.currentTimeMillis();
		if (ttlMillis > 0) {
			// 添加到黑名单
			tokenBlacklistService.blacklist(jti, ttlMillis/1000);
			log.debug("Token {} blacklisted successfully", jti);
		}
	}
}
