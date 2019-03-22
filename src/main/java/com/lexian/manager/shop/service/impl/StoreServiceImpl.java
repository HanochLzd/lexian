package com.lexian.manager.shop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lexian.manager.shop.bean.Store;
import com.lexian.manager.shop.dao.StoreDao;
import com.lexian.manager.shop.service.StoreService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class StoreServiceImpl implements StoreService {
    @Resource
    private StoreDao storeDao;

    @Override
    public ResultHelper getAllStore(Page page) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("page", page);
        List<Store> list = storeDao.getAllStorePage(params);
        page.setData(list);
        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper addStore(Store store) {

        Store sto = storeDao.getStoreByStoreNo(store.getStoreNo());
        if (sto != null) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            storeDao.addStore(store);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }

    }

    @Override
    public ResultHelper updateStore(Store store) {
        storeDao.updateStore(store);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    @Override
    public ResultHelper getStoresByCitysId(Integer provinceId, Integer cityId, Integer countyId) {
        return new ResultHelper(Constant.CODE_SUCCESS, storeDao.getStoresByCitysId(provinceId, cityId, countyId));
    }

    @Override
    public ResultHelper getStoreByStoreNo(String storeNo) {
        Store store = storeDao.getStoreByStoreNo(storeNo);
        return new ResultHelper(Constant.CODE_SUCCESS, store);
    }

}
