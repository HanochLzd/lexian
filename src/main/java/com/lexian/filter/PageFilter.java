package com.lexian.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author luozidong
 */
public class PageFilter implements Filter {

    private String homePage;
    private String root;
    private String loginPage;

    @Override
    public void init(FilterConfig filterConfig) {

        final String homePageStr = "home-page";
        final String loginPageStr = "login-page";

        homePage = filterConfig.getInitParameter(homePageStr);
        loginPage = filterConfig.getInitParameter(loginPageStr);

        root = filterConfig.getServletContext().getContextPath();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        final String separator = "/";

        String url = req.getRequestURI();

        if (url.equals(root + separator) || url.contains(loginPage)) {
            if (isLogin(req, resp)) {
                resp.sendRedirect(root + separator + homePage);
                return;
            }
        } else if (url.contains(homePage)) {
            if (!isLogin(req, resp)) {
                resp.sendRedirect(root + separator + loginPage);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isLogin(HttpServletRequest req, HttpServletResponse resp) {
        boolean goHome = true;
        final String managerId = "managerId";
        HttpSession session = req.getSession();
        if (session.getAttribute(managerId) == null) {
            goHome = false;
        }

        return goHome;
    }

    @Override
    public void destroy() {
    }

}
