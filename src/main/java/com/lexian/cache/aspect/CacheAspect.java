package com.lexian.cache.aspect;

import com.lexian.cache.annotation.Cacheable;
import com.lexian.cache.service.RedisCacheService;
import com.lexian.cache.util.CacheKeyUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Author :lzd
 * @Date : 2018/12/10 11:43
 * @Description :
 */
@Aspect
@Component
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);


    @Resource
    private RedisCacheService redisCacheService;

    /**
     * 缓存切入点
     *
     * @param joinPoint 切点
     * @return
     */
    @Around("@annotation(com.lexian.cache.annotation.Cacheable)")
    public Object process(ProceedingJoinPoint joinPoint) {
        Object result = null;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        try {
            if (method.getParameterTypes().length == 0) {
                return joinPoint.proceed();
            }
            //获取注解
            Cacheable cacheable = getAnnotation(method);
            if (cacheable == null) {
                return joinPoint.proceed();
            }

            String key = generateKey(method, joinPoint);

            result = redisCacheService.getCache(key);

            System.out.println("读缓存------>" + result);

            if (result == null) {
                //缓存中不存在,查询，然后放入缓存
                int expireTime = cacheable.expireTime();
                //查询数据库等
                result = joinPoint.proceed();
                //放入缓存--后续可以修改为异步
                System.out.println("写缓存------>" + key + " : " + result);
                redisCacheService.putCache(key, result, expireTime);
                return result;
            } else {
                return result;
            }
        } catch (Throwable throwable) {
            logger.error("execute cache point is failed ", throwable);
        }
        return result;
    }

    /**
     * 根据参数类型与值动态生成key
     *
     * @param method
     * @param joinPoint
     * @return
     */
    private String generateKey(Method method, ProceedingJoinPoint joinPoint) {

        JSONObject paramJson = new JSONObject();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < params.length; i++) {
            try {
                paramJson.put(params[i], args[i]);
            } catch (Exception e) {
                logger.error("execute logging point is failed:(", e);
            }
        }
        return CacheKeyUtil.getKey(method.getReturnType(), method.getName(),paramJson);
    }


    /**
     * 获取注解
     *
     * @param method
     * @return
     */
    private Cacheable getAnnotation(Method method) {

        return method.getAnnotation(Cacheable.class);
    }
}
