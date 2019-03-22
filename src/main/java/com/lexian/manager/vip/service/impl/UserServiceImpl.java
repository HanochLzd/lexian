package com.lexian.manager.vip.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lexian.manager.vip.bean.User;
import com.lexian.manager.vip.dao.UserDao;
import com.lexian.manager.vip.service.UserService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public ResultHelper getUsers(Page page) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<User> privileges = userDao.getUsersPage(params);
        page.setData(privileges);

        return new ResultHelper(Constant.CODE_SUCCESS, page);

    }

    @Override
    public ResultHelper updateUser(User user) {
        userDao.updateUser(user);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

}
