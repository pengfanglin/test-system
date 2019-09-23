package com.project.others;

import com.project.annotation.LocalCache;
import com.project.annotation.LocalCacheRemove;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 本地缓存切面类，首先从缓存中取数据，数据存在返回缓存数据，否则去数据库取
 * 
 * @author 方林
 *
 */
@Component
@Aspect()
public class LocalCacheAop {
	/**
	 * 本地缓存仓库
	 */
	private static Map<String,Object> cache=new LinkedHashMap<>();
	/**
	 * LocalCache切入点规则
	 */
	@Pointcut(value = "@annotation(com.project.annotation.LocalCache)")
	public void pointLocalCache() {

	}
	/**
	 * LocalCacheRemove切入点规则
	 */
	@Pointcut(value = "@annotation(com.project.annotation.LocalCacheRemove)")
	public void pointLocalCacheRemove() {

	}
	/**
	 * 切入的验证代码
	 */
	@Around(value = "pointLocalCache()")
	public Object localCacheAop(ProceedingJoinPoint point) throws Throwable{
		MethodSignature joinPointObject = (MethodSignature) point.getSignature();
		Method method = joinPointObject.getMethod();
		LocalCache localCache=method.getAnnotation(LocalCache.class);
		Object object=cache.get(localCache.value());
		if(object==null){
			object=point.proceed();
			cache.put(localCache.value(),object);
		}
		return object;
	}
	/**
	 * 切入的验证代码
	 */
	@Around(value = "pointLocalCacheRemove()")
	public Object localCacheRemoveAop(ProceedingJoinPoint point) throws Throwable{
		MethodSignature joinPointObject = (MethodSignature) point.getSignature();
		Method method = joinPointObject.getMethod();
		LocalCacheRemove localCacheRemove=method.getAnnotation(LocalCacheRemove.class);
		Object object=cache.get(localCacheRemove.value());
		if(object!=null){
			cache.remove(localCacheRemove.value());
		}
		object=point.proceed();
		return object;
	}
}
