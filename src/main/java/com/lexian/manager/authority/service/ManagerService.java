package com.lexian.manager.authority.service;

import com.lexian.cache.annotation.Cacheable;
import com.lexian.manager.authority.bean.Manager;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;
import org.apache.ibatis.annotations.ConstructorArgs;

/**
 * @author luozidong
 */
public interface ManagerService {

    @Cacheable
    ResultHelper signIn(String name, String password);

    @Cacheable
    ResultHelper getPrivileges(Integer id, Page page);

    @Cacheable
    ResultHelper getPrivilegeUrls(Integer id);

    @Cacheable
    ResultHelper getUserWithMenus(Integer id);

    ResultHelper addManager(Manager manager, Integer[] roleId);

    ResultHelper updateManager(Manager manager);

    @Cacheable
    ResultHelper getManagers(Page page);

    ResultHelper deleteManagerById(Integer id);

    ResultHelper verifyPassword(Integer id, String password);

    ResultHelper updateManagerPassword(Manager manager, String newPass);

    @Cacheable
    ResultHelper getMenus(Integer id, Page page);

    ResultHelper updateAssociatedRole(Manager manager, Integer[] newRoleId);


}
