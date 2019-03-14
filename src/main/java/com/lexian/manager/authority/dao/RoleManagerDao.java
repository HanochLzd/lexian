package com.lexian.manager.authority.dao;

import java.util.List;

import com.lexian.manager.authority.bean.RoleManager;

/**
 * @author luozidong
 */
public interface RoleManagerDao {

    void insertRoleManagerBatch(List<RoleManager> rms);

    void deleteRoleManagerByManagerId(Integer managerId);

}
