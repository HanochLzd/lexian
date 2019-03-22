package com.lexian.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luozidong
 */
public class Constant {

    public static Map<Integer, String> messages;

    public static final int CODE_SUCCESS = 1;                // 操作成功
    public static final int CODE_ENTITY_NOT_FOUND = -1;        // 找不到实体
    public static final int CODE_INVALID_PARAMETER = -2;    // 参数无效
    public static final int CODE_ENTITY_DUPLICATED = -3;    // 该实体已经存在，因此不能再次创建
    public static final int CODE_ENTITY_IN_USE = -4;        // 该实体仍被使用，因此不能被删除
    public static final int CODE_PERMISSION_DENIED = -5;    // 没有执行该操作的权限
    public static final int CODE_LOGIN_REQUIRED = -6;        // 用户没有登录
    public static final int CODE_LOGIN_FAILED = -7;            // 登录失败
    public static final int CODE_EXECUTE_ERROR = -99;        // 服务器执行出错
    public static final int CODE_UNKNOWN_REASON = -100;        // 不明原因的错误

    public static final int CODE_STATE_FORBID = -200;

    public static final int CODE_NO_PRIVILEGE = -10;        //沒有权限
    public static final int CODE_NO_LOGIN = -101;

    static {
        messages = new HashMap<>();
        messages.put(CODE_SUCCESS, "操作成功");
        messages.put(CODE_ENTITY_NOT_FOUND, "找不到实体");
        messages.put(CODE_INVALID_PARAMETER, "参数无效");
        messages.put(CODE_ENTITY_DUPLICATED, "该实体已经存在");
        messages.put(CODE_ENTITY_IN_USE, "该实体不能删除");
        messages.put(CODE_PERMISSION_DENIED, "没有执行该操作的权限");
        messages.put(CODE_LOGIN_REQUIRED, "用户没有登录");
        messages.put(CODE_LOGIN_FAILED, "登录失败");
        messages.put(CODE_EXECUTE_ERROR, "服务器执行出错");
        messages.put(CODE_UNKNOWN_REASON, "未知的原因");
        messages.put(CODE_NO_PRIVILEGE, "沒有权限");
        messages.put(CODE_STATE_FORBID, "该用户已被禁用，请联系管理员");
        messages.put(CODE_NO_LOGIN, "请先登录");
    }
}
