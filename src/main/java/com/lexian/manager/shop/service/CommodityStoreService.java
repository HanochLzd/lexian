package com.lexian.manager.shop.service;

import java.util.List;

import com.lexian.manager.shop.bean.CommodityStore;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

public interface CommodityStoreService {

    ResultHelper getCommodityByStoreNo(String storeNo, Page page);

    ResultHelper updateCommodityStore(CommodityStore commodityStore);

    ResultHelper addCommodityStore(List<CommodityStore> list);

    ResultHelper getCommodityByCategoryId(int categoryId, String storeNo);

    ResultHelper registCommodityStore(String storeNo, String[] commodityNo, Integer type);
}
 