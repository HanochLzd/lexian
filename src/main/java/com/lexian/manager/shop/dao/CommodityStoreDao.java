package com.lexian.manager.shop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.manager.shop.bean.CommodityStore;

public interface CommodityStoreDao {
    List<CommodityStore> getCommdityByStoreNoPage(Map<String, Object> params);

    void updateCommdityStore(CommodityStore commoditystore);

    void addCommodityStore(CommodityStore commoditystore);

    List<Commodity> getCommodityByCategoryId(@Param("categoryId") int categoryId, @Param("storeNo") String storeNo);

    void registCommodityStore(@Param("storeNo") String storeNo, @Param("commodityNo") String commodityNo, @Param("type") Integer type);
}
