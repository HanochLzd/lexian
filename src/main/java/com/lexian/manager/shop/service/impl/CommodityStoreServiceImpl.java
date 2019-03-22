package com.lexian.manager.shop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexian.utils.UrlConstant;
import org.springframework.stereotype.Service;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.manager.shop.bean.CommodityStore;
import com.lexian.manager.shop.dao.CommodityStoreDao;
import com.lexian.manager.shop.service.CommodityStoreService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class CommodityStoreServiceImpl implements CommodityStoreService {
    @Resource
    private CommodityStoreDao commodityStoreDao;


    @Override
    public ResultHelper getCommodityByStoreNo(String storeNo, Page page) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("page", page);
        params.put("storeNo", storeNo);
        List<CommodityStore> list = commodityStoreDao.getCommdityByStoreNoPage(params);
        page.setData(list);
        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper updateCommodityStore(CommodityStore commodityStore) {
        commodityStoreDao.updateCommdityStore(commodityStore);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    @Override
    public ResultHelper addCommodityStore(List<CommodityStore> list) {
        for (CommodityStore commodityStore : list) {
            commodityStoreDao.addCommodityStore(commodityStore);
        }
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    @Override
    public ResultHelper getCommodityByCategoryId(int categoryId, String storeNo) {
        List<Commodity> list = commodityStoreDao.getCommodityByCategoryId(categoryId, storeNo);
        for (Commodity commodity : list) {
            commodity.setPictureUrl(UrlConstant.QI_NIU_URL + commodity.getPictureUrl());
        }
        return new ResultHelper(Constant.CODE_SUCCESS, list);
    }

    @Override
    public ResultHelper registCommodityStore(String storeNo, String[] commodityNo, Integer type) {
        for (String string : commodityNo) {
            commodityStoreDao.registCommodityStore(storeNo, string, type);
        }
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

}
