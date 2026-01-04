package cn.wurock.blog.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThreadLocal工具类，可用来存储当前线程的用户数据
 */
@SuppressWarnings("unchecked")
public final class ThreadLocalUtil {
	// 提供ThreadLocal 对象
	private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL
			= ThreadLocal.withInitial(ConcurrentHashMap::new);
	
	// 存储键值对
	public static void set(String key, Object obj) {
		THREAD_LOCAL.get().put(key, obj);
	}
	
	// 根据键获取值
	public static <T> T get(String key) {
		return (T) THREAD_LOCAL.get().get(key);
	}
	
	// 移除某个key
	public static void remove(String key) {
		Map<String, Object> map = THREAD_LOCAL.get();
		map.remove(key);
	}
	
	// 清除当前线程所有ThreadLocal数据，防止内存泄露
	public static void clear() {
		THREAD_LOCAL.remove();
	}
	
	// 判断某个key是否存在
	public static boolean containsKey(String key) {
		return THREAD_LOCAL.get().containsKey(key);
	}
}
