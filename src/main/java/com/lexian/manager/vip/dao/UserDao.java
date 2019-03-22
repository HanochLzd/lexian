package com.lexian.manager.vip.dao;

import java.util.List;
import java.util.Map;

import com.lexian.manager.vip.bean.User;

public interface UserDao {

    List<User> getUsersPage(Map<String, Object> params);

    void updateUser(User user);
}
