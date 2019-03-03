/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.manager.vip.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lexian.manager.vip.bean.User;
import com.lexian.manager.vip.dao.UserDao;
import com.lexian.manager.vip.service.UserService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * 
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 郝伟
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public ResultHelper getUsers(Page page) {

		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		List<User> privileges = userDao.getUsersPage(params);
		page.setData(privileges);

		ResultHelper result = new ResultHelper(Constant.CODE_SUCCESS, page);

		return result;

	}

	@Override
	public ResultHelper updateUser(User user) {
		userDao.updateUser(user);
		return new ResultHelper(Constant.CODE_SUCCESS);
	}

}
