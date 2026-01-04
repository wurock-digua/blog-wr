package cn.wurock.blog.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 令牌黑名单服务
 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
	private final RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 将令牌加入黑名单
	 * @param jti 令牌ID
	 * @param expirationTimeSeconds 过期时间（秒）
	 */
	public void blacklist(String jti, long expirationTimeSeconds) {
		if (jti != null && !jti.isBlank()) {
			String key = "blacklist:jti:" + jti;
			redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(expirationTimeSeconds));
		}
	}
	
	/**
	 * 检查令牌是否在黑名单中
	 * @param jti 令牌ID
	 * @return true 令牌在黑名单中
	 */
	public boolean isBlacklisted(String jti) {
		if (jti == null || jti.isBlank()) return false;
		String key = "blacklist:jti:" + jti;
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
