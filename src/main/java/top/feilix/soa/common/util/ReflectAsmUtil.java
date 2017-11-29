package top.feilix.soa.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @author feilix
 *
 */
public class ReflectAsmUtil {

	public static final ConcurrentHashMap<String, MethodAccess> maCache = new ConcurrentHashMap<String, MethodAccess>();
	public static final ConcurrentHashMap<String, FieldAccess> faCache = new ConcurrentHashMap<String, FieldAccess>();

	/**
	 * 缓存MethodAccess对象
	 * 用于提升性能
	 * 线程安全
	 * 
	 * @param clazz
	 * @return
	 */
	private static <T> MethodAccess cacheMethodAccess(Class<T> clazz) {
		MethodAccess ma = maCache.get(clazz.getName());
		if(null == ma) {
			maCache.putIfAbsent(clazz.getName(), MethodAccess.get(clazz));
			ma = maCache.get(clazz.getName());
		}
		return ma;
	}
	
	/**
	 * 缓存FieldAccess对象
	 * 用于提升性能
	 * 线程安全
	 * 
	 * @param clazz
	 * @return
	 */
	private static <T> FieldAccess cacheFieldAccess(Class<T> clazz) {
		FieldAccess fa = faCache.get(clazz.getName());
		if(null == fa) {
			faCache.putIfAbsent(clazz.getName(), FieldAccess.get(clazz));
			fa = faCache.get(clazz.getName());
		}
		return fa;
	}
	
	/**
	 * JavaBean属性复制
	 * 
	 * @param src
	 * @param dest
	 */
	public static <T, K> void copyProperties(T src, K dest) {
		FieldAccess accessSrc = cacheFieldAccess(src.getClass());
		FieldAccess accessDest = cacheFieldAccess(dest.getClass());
		String[] fieldNamesDest = accessDest.getFieldNames();
		for(String fieldName : fieldNamesDest) {
			accessDest.set(dest, fieldName, accessSrc.get(src, fieldName));
		}
	}

}
