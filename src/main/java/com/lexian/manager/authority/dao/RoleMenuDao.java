package com.lexian.manager.authority.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.authority.bean.RoleMenu;

/**
 * @author luozidong
 */
public interface RoleMenuDao {

    List<RoleMenu> getRoleMenus(int id);

    void insertByBatch(List<RoleMenu> rms);

    void deleteByBatch(@Param("roleId") int roleId, @Param("rms") List<RoleMenu> rms);

}
