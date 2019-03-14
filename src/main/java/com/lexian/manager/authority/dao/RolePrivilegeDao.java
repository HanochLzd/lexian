package com.lexian.manager.authority.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.authority.bean.RolePrivilege;

/**
 * @author luozidong
 */
public interface RolePrivilegeDao {

    List<RolePrivilege> getRolePrivileges(int id);

    void insertByBatch(List<RolePrivilege> rps);

    void deleteByBatch(@Param("roleId") int roleId, @Param("rps") List<RolePrivilege> rps);
}
