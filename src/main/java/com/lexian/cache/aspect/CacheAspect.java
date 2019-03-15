package com.lexian.cache.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import suishen.koi.common.cache.annotation.Cacheable;
import suishen.koi.common.cache.service.RedisCacheService;
import suishen.koi.common.cache.util.CacheKeyUtil;

import java.lang.reflect.Method;

/**
 * @Author :lwy
 * @Date : 2018/12/10 11:43
 * @Description :
 */
@Aspect
@Component
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);


    @Autowired
    private RedisCacheService redisCacheService;

    /**
     * 缓存切入点
     *
     * @param joinPoint 切点
     * @return
     */
    @Around("@annotation(suishen.koi.common.cache.annotation.Cacheable)")
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
            if (result == null) {
                //缓存中不存在,查询，然后放入缓存
                int expireTime = cacheable.expireTime();
                //查询数据库等
                result = joinPoint.proceed();
                //放入缓存--后续可以修改为异步
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

        return CacheKeyUtil.getKey(method.getReturnType(), joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getArgs());
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
