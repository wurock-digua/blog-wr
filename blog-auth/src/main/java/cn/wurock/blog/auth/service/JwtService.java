package cn.wurock.blog.auth.service;

import cn.wurock.blog.config.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtService {
	
	private final JwtProperties jwtProperties;
	private final TokenBlacklistService tokenBlacklistService;
	
	/**
	 * 获取签名密钥
	 */
	private SecretKey getSigningKey() {
		String secret = jwtProperties.getSecret();
		if (secret == null || secret.trim().isEmpty()) {
			throw new IllegalArgumentException("JWT secret cannot be null or empty");
		}
		// 推荐使用 Base64 编码的密钥
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	/**
	 * 获取签名算法
	 */
	private SignatureAlgorithm getSignatureAlgorithm() {
		String alg = jwtProperties.getAlgorithm();
		try {
			return SignatureAlgorithm.valueOf(alg.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unsupported JWT algorithm: " + alg);
		}
	}
	
	/**
	 * 生成 JWT 令牌
	 */
	public String generateToken(UserDetails userDetails) {
		String jti = UUID.randomUUID().toString();
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setId(jti)
				.setIssuedAt(new Date()) // 令牌创建时间
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000)) // 令牌过期时间
				.signWith(getSigningKey(), getSignatureAlgorithm()) // 使用指定的算法和密钥签名令牌
				.compact(); // 生成令牌
	}
	
	/**
	 * 生成刷新令牌 (Refresh Token)
	 */
	public String generateRefreshToken(UserDetails userDetails) {
		String jti = UUID.randomUUID().toString();
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setId(jti)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration() * 1000))
				.signWith(getSigningKey(), getSignatureAlgorithm())
				.compact();
	}
	
	/**
	 * 解析JWT 令牌(不验证黑名单)
	 */
	public Claims parseToken(String token) throws ExpiredJwtException, MalformedJwtException, SignatureException {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	/**
	 * 验证 JWT 令牌是否有效且属于指定用户
	 * @param token       JWT 字符串
	 * @param userDetails 当前尝试认证的用户详情（通常从数据库加载）
	 * @return true 表示令牌有效且匹配用户
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			// 1. 解析并验证令牌（包括签名、过期时间等）
			Claims claims = parseToken(token); // 使用你已有的 parseToken 方法
			
			// 2. 检查 subject 是否匹配
			String username = claims.getSubject();
			if (username == null || !username.equals(userDetails.getUsername())) {
				return false;
			}
			
			// 3. 检查 jti 是否被吊销（如实现登出功能）
			 String jti = claims.getId();
			 if (jti != null && tokenBlacklistService.isBlacklisted(jti)) {
			     return false;
			 }
			
			return true;
			
		} catch (ExpiredJwtException e) {
			// 令牌已过期
			return false;
		} catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
			// 令牌格式错误、签名无效或参数非法
			return false;
		}
	}
	
	/**
	 * 从请求头中提取令牌
	 */
	public String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtProperties.getHeader());
		if (bearerToken != null && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
			return bearerToken.substring(jwtProperties.getTokenPrefix().length());
		}
		return null;
	}
	
	/**
	 * 从令牌中提取用户名
	 */
	public String extractUsername(String token) throws SignatureException {
		Claims claims = parseToken(token); // 会抛异常
		return claims.getSubject();
	}
}
