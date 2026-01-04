package cn.wurock.blog.auth.utils;

import cn.wurock.blog.auth.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 工具类
 */
public class SecurityContextUtil {
	
	/**
	 * 安全地获取当前认证的 CustomUserDetails 对象
	 */
	public static CustomUserDetails getCurrentUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			throw new IllegalStateException("当前无认证信息，请先登录");
		}
		if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
			throw new IllegalStateException("当前认证主体不是 CustomUserDetails 类型");
		}
		return (CustomUserDetails) auth.getPrincipal();
	}
	
	
	/**
	 * 获取当前用户ID
	 * @return
	 */
	public static Long getCurrentUserId() {
		return getCurrentUserDetails().getUserId();
	}
	
	/**
	 * 获取当前用户名
	 * @return
	 */
	public static String getCurrentUsername() {
		return getCurrentUserDetails().getUsername();
	}
}
