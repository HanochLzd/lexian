
package com.lexian.manager.shop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.shop.bean.CommodityStore;
import com.lexian.manager.shop.service.CommodityStoreService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("commoditystore")
public class CommodityStoreController {

    @Resource
    private CommodityStoreService commoditystoreService;

    @ResponseBody
    @RequestMapping("getCommodityStoreByStoreNo.do")
    public ResultHelper getCommdityByStoreNo(Page page, String storeNo) {
        return commoditystoreService.getCommodityByStoreNo(storeNo, page);
        // commoditystore/getCommodityStoreByStoreNo.do?storeNo=1013&pageNo=10
    }

    @ResponseBody
    @RequestMapping("updateCommodityStore.do")
    public ResultHelper updateCommodityStore(CommodityStore commoditystore) {
        return commoditystoreService.updateCommodityStore(commoditystore);
        //commoditystore/updateCommodityStore.do?id=8363&realPrice=11.11
    }

    @ResponseBody
    @RequestMapping("addCommodityStore.do")
    public ResultHelper addCommodityStore(List<CommodityStore> list) {
        return commoditystoreService.addCommodityStore(list);
        //commoditystore/addCommodityStore.do
    }

    @ResponseBody
    @RequestMapping("getCommodityByCategoryId.do")
    public ResultHelper getCommodityByCategoryId(int categoryId, String storeNo) {

        return commoditystoreService.getCommodityByCategoryId(categoryId, storeNo);
        //commoditystore/getCommodityByCategoryId.do?categoryId=226&storeNo=1001
    }

    @ResponseBody
    @RequestMapping("registCommodityStore.do")
    public ResultHelper registCommodityStore(String storeNo, String[] commodityNo, Integer type) {
        return commoditystoreService.registCommodityStore(storeNo, commodityNo, type);
        //commoditystore/registCommodityStore.do
    }
}
