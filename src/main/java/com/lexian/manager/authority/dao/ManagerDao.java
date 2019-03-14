package com.lexian.manager.authority.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.authority.bean.Manager;
import com.lexian.manager.authority.bean.Menu;
import com.lexian.manager.authority.bean.Privilege;

/**
 * @author luozidong
 */
public interface ManagerDao {

    Manager getManagerByNameAndPassword(@Param("name") String name, @Param("password") String password);


    List<Privilege> getPrivilegesPage(Map<String, Object> params);

    Manager getUserWithMenus(int id);

    void addManager(Manager manager);

    void updateManager(Manager manager);

    List<Manager> getManagersPage(Map<String, Object> params);

    List<String> getPrivilegeUrls(int id);


    void deleteManagerById(int id);


    Integer verifyPassword(@Param("id") Integer id, @Param("password") String password);


    List<Menu> getMenusPage(Map<String, Object> params);

    Integer hasNameUsed(String name);
}
