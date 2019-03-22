package com.lexian.manager.plate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.plate.service.SpeCommodityService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("speCommodity")
public class SpeCommodityController {

    @Autowired
    private SpeCommodityService speCommodityService;

    /**
     * 通过传回来的special表的id与special_commodity表的specialid匹配
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("getSpecialCommodities.do")
    public ResultHelper getSpecialCommodities(int id, Page page) {

        return speCommodityService.getSpecialCommodities(id, page);
        //speCommodity/getSpecialCommodities.do?id=1&pageNo=1
    }

    /**
     * id 为special_commodity表的id，从special_commodity表删除，删除special与该commodity的关系
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteSpeCommodity.do")
    public ResultHelper deleteSpeCommodity(int id) {
        return speCommodityService.deleteSpeCommodity(id);
        //speCommodity/deleteSpeCommodity.do?id=
    }

    /**
     * 通过选择commodity获取commodityNo和返回specialId来添加（表special_commodity）
     *
     * @param commodityNo
     * @param specialId
     * @return
     */
    @ResponseBody
    @RequestMapping("addSpecialCommodities.do")
    public ResultHelper addSpecialCommodities(String commodityNo, int specialId) {
        return speCommodityService.addSpecialCommodities(commodityNo, specialId);
        //speCommodity/addSpecialCommodities.do?commodityNo=6923644267148&specialId=1
    }
}
