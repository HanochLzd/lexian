package com.lexian.manager.authority.controller;

import javax.validation.Valid;

import com.lexian.manager.authority.service.impl.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.authority.bean.Manager;
import com.lexian.manager.authority.service.ManagerService;
import com.lexian.manager.authority.service.RoleService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author luozidong
 */
@Controller
@RequestMapping("handleManager")
public class HandleManagerController {
    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private RoleService roleService;

    /**
     * handleManager/getManagers.do
     */
    @ResponseBody
    @RequestMapping("getManagers.do")
    public ResultHelper getManagers(Page page) {
        return managerService.getManagers(page);
    }

    //handleManager/addManager.do?name=xxx
    //<insert id="insertAndGetId" useGeneratedKeys="true" keyProperty="userId" parameterType="com.chenzhou.mybatis.User">
    @ResponseBody
    @RequestMapping("addManager.do")
    public ResultHelper addManager(@Valid Manager manager, Integer[] roleId) {
        //TODO delete
        System.out.println(manager);
        return managerService.addManager(manager, roleId);
    }

    //handleManager/updateManager.do?id=82&name=1
    @ResponseBody
    @RequestMapping("updateManager.do")
    public ResultHelper updateManager(@Valid Manager manager) {
        return managerService.updateManager(manager);
    }

    //handleManager/deleteManager.do?id=110
    @ResponseBody
    @RequestMapping("deleteManager.do")
    public ResultHelper deleteManager(int id) {
        return managerService.deleteManagerById(id);
    }

    @ResponseBody
    @RequestMapping("updateAssociatedRole.do")
    public ResultHelper updateAssociatedRole(Manager manager, Integer[] newRoleId) {
        return managerService.updateAssociatedRole(manager, newRoleId);
    }

    @ResponseBody
    @RequestMapping("getRoleByManagerId.do")
    public ResultHelper getRoleByManagerId(Integer managerId) {
        return roleService.getRoleByManagerId(managerId);
    }
}
