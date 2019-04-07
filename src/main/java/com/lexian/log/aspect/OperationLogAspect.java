package com.lexian.log.aspect;
//
//import com.alibaba.fastjson.JSONObject;
//import com.lexian.log.log.service.RiskControlOperationLogService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import suishen.auth.meta.SuishenUser;
//import suishen.auth.util.SecurityContextUtil;
//import suishen.libs.meta.JSONResult;
//import suishen.libs.util.JSONUtil;
//import suishen.libs.web.meta.ActionStatus;
//import weli.risk.common.enums.DateFormatEnum;
//import weli.risk.common.util.DateUtils;
//import weli.risk.log.annotation.LogAnnotation;
//import weli.risk.log.meta.RiskControlOperationLog;
//import weli.risk.log.service.RiskControlOperationLogService;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Method;
//import java.util.Objects;
//
///**
// * @Author: luozidong
// * @Date: 2019/2/22
// * @Time: 10:06
// * Copyright © SSY All Rights Reserved.
// */
//@Aspect
//@Component
public class OperationLogAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);
//
//    @Resource
//    private RiskControlOperationLogService riskControlOperationLogService;
//
//
//    @Pointcut("@annotation(weli.risk.log.annotation.LogAnnotation)")
//    private void accAspect() {
//    }
//
//    @AfterReturning(pointcut = "accAspect()", returning = "result")
//    public void around(JoinPoint joinPoint, Object result) {
//        try {
//            //获取当前用户信息
//            SuishenUser suishenUser = SecurityContextUtil.getSessionUser();
//
//            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//
//            Method method = methodSignature.getMethod();
//
//            //获得注解
//            LogAnnotation logAnnotation = getAnnotation(method);
//
//            String logType = logAnnotation.logType().name();
//
//            JSONResult jsonResult = (JSONResult) result;
//
//            if (Objects.isNull(jsonResult)) {
//                return;
//            }
//
//            if (jsonResult.getStatus() != ActionStatus.NORMAL_RETURNED.inValue()) {
//                return;
//            }
//
//            Object[] args = joinPoint.getArgs();
//            String[] paramNames = methodSignature.getParameterNames();
//
//            Long currentTimeMillis = System.currentTimeMillis();
//
//            RiskControlOperationLog log = RiskControlOperationLog.builder()
//                    .account(String.valueOf(suishenUser.getUid()))
//                    .userName(suishenUser.getUserName())
//                    .data(getParameters(args, paramNames))
//                    .type(logType)
//                    .createTime(currentTimeMillis)
//                    .date(DateUtils.formatDate(currentTimeMillis, DateFormatEnum.yyyyMMdd))
//                    .build();
//            riskControlOperationLogService.addOperationLog(log);
//        } catch (Exception e) {
//            logger.error("execute logging point is failed:(", e);
//        }
//    }
//
//    /**
//     * 获取注解
//     *
//     * @param method
//     * @return
//     */
//    private LogAnnotation getAnnotation(Method method) {
//        return method.getAnnotation(LogAnnotation.class);
//    }
//
//
//    /**
//     * 获取参数数据
//     *
//     * @param args
//     * @return
//     */
//    private String getParameters(Object[] args, String[] paramNames) {
//        if (Objects.isNull(args) || args.length <= 0) {
//            return "";
//        }
//
//        if (args.length != paramNames.length) {
//            return "";
//        }
//
//        int paramsCount = args.length;
//
//        JSONObject paramJson = new JSONObject();
//
//        for (int i = 0; i < paramsCount; i++) {
//            //获取类的全限定名
//            String classCanonicalName = args[i].getClass().getCanonicalName();
//            if (classCanonicalName.startsWith("org.springframework")
//                    || classCanonicalName.startsWith("org.apache.catalina")
//                    || classCanonicalName.startsWith("javax")
//                    || classCanonicalName.startsWith("java.io")) {
//                //特殊类型参数不需要记录
//                continue;
//            }
//            if (args.getClass().isInterface()) {
//                continue;
//            }
//            try {
//                paramJson.put(paramNames[i], JSONUtil.getJsonString(args[i]));
//            } catch (Exception e) {
//                logger.error("execute logging point is failed:(", e);
//            }
//        }
//        return paramJson.toJSONString();
//    }
//
}
