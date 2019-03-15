package com.lexian.cache.util;

/**
 * @Author :lwy
 * @Date : 2018/12/10 14:18
 * @Description :缓存key生成策略
 */
public class CacheKeyUtil {

    /**
     * 生成key策略
     *
     * @param returnType
     * @param args
     * @return
     */
    public static String getKey(Class<?> returnType, Object ...args) {
        String key = null;

        if (returnType == null) {
            returnType = Object.class;
        }

        key = getKey(returnType.getName(), args);
        return key;
    }

    private static String getKey(String name, Object[] fieldValues) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (Object obj : fieldValues) {
            if (obj != null && !"null".equalsIgnoreCase(obj.toString())) {
                sb.append("_");
                sb.append(obj);
            }
        }
        return sb.toString();
    }
}
