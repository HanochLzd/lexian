package com.lexian.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;


/**
 * @author luozidong
 */
public class SessionContext {

    private static Map<Integer, HttpSession> sessions = new HashMap<>();
    private static SessionContext instance = new SessionContext();

    private SessionContext() {
    }

    public static SessionContext getInstance() {
        return instance;
    }

    public void addSession(Integer managerId, HttpSession httpSession) {
        sessions.put(managerId, httpSession);
    }

    public void deleteSession(Integer managerId) {
        sessions.remove(managerId);
    }

    public HttpSession getSessionByManagerId(Integer managerId) {
        return sessions.get(managerId);
    }

}
