package com.lexian.manager.goods.controller;

import com.lexian.manager.goods.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lexian.manager.goods.service.CategoryViewService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author luozidong
 */
@Controller
@RequestMapping("categoryView")
@SessionAttributes(value = {"isLogining"}, types = {Boolean.class})
public class CategoryViewController {

    @Autowired
    private CategoryViewService cateViewService;

    @Resource
    private SortService sortService;


    public CategoryViewService getCateViewService() {
        return cateViewService;
    }

    public void setCateViewService(CategoryViewService cateViewService) {
        this.cateViewService = cateViewService;
    }

    /**
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getAllCategoryView.do")
    public ResultHelper getAllCategoryView(Page page) {
        ResultHelper result = cateViewService.getAllCategoryView(page);
        return result;
        //categoryView/getAllCategoryView.do?pageNo=1
    }

    /**
     * 一级菜单
     *
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCategoryView.do")
    public ResultHelper getCategoryView(Page page) {
        return sortService.getCategoryView(page);
        //categoryView/getFirstCategoryView.do?pageNo=1
    }

    /**
     * 二级菜单
     *
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getSecondCategoryView.do")
    public ResultHelper getSecondCategoryView(Page page) {
        ResultHelper result = cateViewService.getSecondCategoryView(page);
        return result;
        //categoryView/getSecondCategoryView.do?pageNo=1
    }

    /**
     * 三级菜单
     *
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getThirdCategoryView.do")
    public ResultHelper getThirdCategoryView(Page page) {
        ResultHelper result = cateViewService.getThirdCategoryView(page);
        return result;
        //categoryView/getThirdCategoryView.do?pageNo=1
    }
}
