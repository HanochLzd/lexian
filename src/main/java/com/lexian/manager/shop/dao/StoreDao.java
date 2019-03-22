package com.lexian.manager.shop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.shop.bean.Store;

public interface StoreDao {
    // Map<String,Object> params
    List<Store> getAllStorePage(Map<String, Object> params);

    Store getStoreByStoreNo(String storeNo);

    void addStore(Store store);

    void updateStore(Store store);

    List<Store> getStoresByCitysId(@Param("pid") Integer provinceId, @Param("cityId") Integer cityId,
                                   @Param("countyId") Integer countyId);
}
