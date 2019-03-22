package com.lexian.manager.statistics.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.statistics.bean.CommodityStatistics;
import com.lexian.manager.statistics.bean.StoreCommodityStatistics;

public interface StatisticsDao {
    List<CommodityStatistics> getCommodityBrowseStatistics(@Param("type") Integer type, @Param("size") Integer size);

    List<CommodityStatistics> getCommodityCollectionStatistics(@Param("type") Integer type, @Param("size") Integer size);

    List<CommodityStatistics> getCommodityBuyStatistics(@Param("type") Integer type, @Param("size") Integer size);

    List<StoreCommodityStatistics> getStoreCommodityBuyStatistics(@Param("storeNo") String storeNo, @Param("type") Integer type, @Param("size") Integer size);

    List<StoreCommodityStatistics> getStoreCommodityCollectionStatistics(@Param("storeNo") String storeNo, @Param("type") Integer type, @Param("size") Integer size);

    Integer getStoreCommodityBuyCount(@Param("storeNo") String storeNo, @Param("type") Integer type);

    Integer getStoreCommodityCollectionCount(@Param("storeNo") String storeNo, @Param("type") Integer type);

    Integer getCommodityBrowseCount(Integer type);

    Integer getCommodityBuyCount(Integer type);

    Integer getCommodityCollectionCount(Integer type);


}
