package com.lexian.manager.authority.service;

import com.lexian.manager.authority.bean.Role;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author luozidong
 */
public interface RoleService {

    ResultHelper getRoles(Page page);

    ResultHelper addRole(Role role);

    ResultHelper updateRole(Role role);


    ResultHelper getMenus(Integer id);

    ResultHelper updateMenus(Integer id, int[] menuIds);

    ResultHelper updatePrivileges(Integer id, int[] privilegeId);

    ResultHelper getPrivileges(Integer id);

    ResultHelper getAllRoles();

    ResultHelper getRoleByManagerId(Integer managerId);
}
