package com.lexian.manager.authority.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.authority.bean.Menu;
import com.lexian.manager.authority.bean.Privilege;
import com.lexian.manager.authority.bean.Role;


public interface RoleDao {

    List<Role> getRolesPage(Map<String, Object> params);

    void addRole(Role role);

    void updateRole(Role role);

    List<Menu> getMenus(@Param("id") Integer id);

    List<Privilege> getPrivileges(@Param("id") Integer id);

    Integer hasNameUsed(String name);

    List<Role> getAllRoles();

    List<Role> getRoleByManagerId(Integer managerId);

}
