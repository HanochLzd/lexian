/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lexian.utils.Constant;
import com.lexian.web.ResultHelper;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 郝伟
 * @version 1.0
 */
public class CheckPrivilegeFilter implements Filter{

	private String ACTION;
	private String loginAction;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ACTION = filterConfig.getInitParameter("action");
		loginAction = filterConfig.getServletContext().getInitParameter("login-action");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse resp=(HttpServletResponse) response;
		
		if(canContinue(req, resp)){
			chain.doFilter(request, response);
		}else{
			
			ResultHelper resultHelper=new ResultHelper(Constant.CODE_NO_PRIVILEGE);
			
			String error="{'code':'"+resultHelper.getCode()+"','data':'"+resultHelper.getData().toString()+"'}";
			
			resp.getWriter().print(error);
		}
	}

	private boolean canContinue(HttpServletRequest req, HttpServletResponse resp) {
		
		boolean canContinue = true;

		if (!req.getRequestURI().toString().contains(loginAction)) {
			canContinue=false;
			String action=req.getParameter(ACTION);
			if(action!=null){
				HttpSession session = req.getSession();
				List<String> privilegeUrls=(List<String>) session.getAttribute("privilegeUrls");
				canContinue=false;
				for(String privilegeUrl:privilegeUrls){
					if(privilegeUrl.endsWith(action)){
						canContinue=true;
						break;
					}
				}
			}
		}

		return canContinue;
		
	}

	@Override
	public void destroy() {
		
	}

}
