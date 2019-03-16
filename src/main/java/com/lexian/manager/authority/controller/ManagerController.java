package com.lexian.manager.authority.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.lexian.manager.authority.service.impl.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.lexian.manager.authority.bean.Manager;
import com.lexian.manager.authority.service.ManagerService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author luozidong
 */
@Controller
@SessionAttributes(value = {"managerId", "privilegeUrls"}, types = {Integer.class, List.class})
@RequestMapping("manager")
public class ManagerController {

    @Resource
    private ManagerServiceImpl managerService;

    /**
     * 登录
     *
     * @param name
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "signIn.do")
    public ResultHelper signIn(String name, String password, HttpSession session, Map<String, Object> model) {

        System.out.println();

        ResultHelper result = managerService.signIn(name, password);

        if (result.getCode() == Constant.CODE_SUCCESS) {

            Manager manager = (Manager) result.getData();

            if (manager.getStatus() == 1) {
                model.put("managerId", manager.getId());
                model.put("privilegeUrls", managerService.getPrivilegeUrls(manager.getId()).getData());
            }
        }


        return result;
    }

    /**
     * 退出登录
     *
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping("signOut.do")
    public ResultHelper signOut(SessionStatus status) {
        status.setComplete();
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    /**
     * 修改密码
     *
     * @param manager
     * @param newPass
     * @param model
     * @return
     */
    // manager/updateManagerPassword.do?password=1&newPass=2
    @ResponseBody
    @RequestMapping("updateManagerPassword.do")
    public ResultHelper updateManagerPassword(@Valid Manager manager, String newPass, Map<String, Object> model) {
        manager.setId((Integer) model.get("managerId"));
        return managerService.updateManagerPassword(manager, newPass);
    }


    /**
     * 获取权限列表
     *
     * @param model
     * @return
     */
    //manager/getPrivileges.do?pageNo=2
    @ResponseBody
    @RequestMapping("getPrivileges.do")
    public ResultHelper getPrivileges(Map<String, Object> model, Page page) {

        return managerService.getPrivileges((Integer) model.get("managerId"), page);
    }

    /**
     * 获取菜单列表
     *
     * @param model
     * @return
     */
    //manager/getUserWithMenus.do
    @ResponseBody
    @RequestMapping("getUserWithMenus.do")
    public ResultHelper getUserWithMenus(Map<String, Object> model) {
        ResultHelper result = managerService.getUserWithMenus((Integer) model.get("managerId"));
        return result;
    }

    /**
     * 获取菜单列表
     *
     * @param model
     * @return
     */
    //manager/getMenus.do
    @ResponseBody
    @RequestMapping("getMenus.do")
    public ResultHelper gethMenus(Map<String, Object> model, Page page) {
        ResultHelper result = managerService.getMenus((Integer) model.get("managerId"), page);
        return result;
    }

}
