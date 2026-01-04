package cn.wurock.blog.common.constant;

/**
 * 白名单路径
 */
public class URLConstant {
	public static final String[] WHITE_LIST = {
			"/api/auth/login",
			"/api/auth/register",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/error"
	};
}
