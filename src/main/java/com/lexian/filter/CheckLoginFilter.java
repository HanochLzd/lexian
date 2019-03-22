package com.lexian.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lexian.context.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luozidong
 * @version 1.0
 */
public class CheckLoginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLoginFilter.class);

    private String loginAction;

    @Override
    public void init(FilterConfig filterConfig) {
        loginAction = filterConfig.getServletContext().getInitParameter("login-action");
        LOGGER.info("Login filter init loginAction - {}", loginAction);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (canContinue(req, resp)) {
            chain.doFilter(request, response);
            if (req.getRequestURI().contains(loginAction)) {
                avoidRepeatLogin(req);
            }
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            String jsonStr = "{\"code\":\"-101\",\"data\":\"请先登录\"}";
            try (PrintWriter out = response.getWriter()) {
                out.write(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void avoidRepeatLogin(HttpServletRequest req) {
        HttpSession session = req.getSession();

        Integer managerId = (Integer) session.getAttribute("managerId");
        if (managerId != null) {
            HttpSession otherSession = SessionContext.getInstance().getSessionByManagerId(managerId);
            if (otherSession != null && !session.equals(otherSession)) {
                otherSession.invalidate();
            }
            SessionContext.getInstance().addSession(managerId, session);
        }
    }

    private boolean canContinue(HttpServletRequest req, HttpServletResponse resp) {
        final String managerId = "managerId";
        boolean canContinue = true;

        LOGGER.info("Login filter URI - {}", req.getRequestURI());

        if (!req.getRequestURI().contains(loginAction)) {
            HttpSession session = req.getSession();
            if (session.getAttribute(managerId) == null) {
                canContinue = false;
            }
        }

        return canContinue;
    }

    @Override
    public void destroy() {

    }

}
