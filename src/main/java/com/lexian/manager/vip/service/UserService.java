package com.lexian.manager.vip.service;

import com.lexian.manager.vip.bean.User;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

public interface UserService {

    ResultHelper getUsers(Page page);

    ResultHelper updateUser(User user);
}
