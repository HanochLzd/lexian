package com.lexian.manager.shop.service;

import com.lexian.manager.shop.bean.Store;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

public interface StoreService {
    ResultHelper getAllStore(Page page);

    ResultHelper getStoreByStoreNo(String storeNo);

    ResultHelper addStore(Store store);

    ResultHelper updateStore(Store store);

    ResultHelper getStoresByCitysId(Integer provinceId, Integer cityId, Integer countyId);
}
