package com.lexian.manager.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.shop.service.CitysService;
import com.lexian.manager.shop.service.StoreService;
import com.lexian.web.ResultHelper;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("city")
public class CitysController {

    @Autowired
    private CitysService cityService;
    @Autowired
    private StoreService storeService;

    @ResponseBody
    @RequestMapping("getCities.do")
    public ResultHelper getCities(Integer parentId) {
        return cityService.getCities(parentId);
    }

    @ResponseBody
    @RequestMapping("getStoresByCitysId.do")
    public ResultHelper getStoresByCitysId(Integer provinceId, Integer cityId, Integer countyId) {
        return storeService.getStoresByCitysId(provinceId, cityId, countyId);
    }
}
