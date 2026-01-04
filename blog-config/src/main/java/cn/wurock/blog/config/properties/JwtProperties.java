package cn.wurock.blog.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	// 密钥 (建议使用 Base64 编码的密钥)
	private String secret;
	
	// 过期时间 (单位：秒)
	private long expiration = 3600;
	
	// 刷新令牌过期时间
	private long refreshExpiration = 604800;
	
	// 签名算法 (可选: HS256, HS384, HS512, RS256 等)
	private String algorithm = "HS256";
	
	// 令牌前缀 (如 Bearer)
	private String tokenPrefix = "Bearer ";
	
	// 请求头名称
	private String header = "Authorization";
}
