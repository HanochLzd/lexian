package com.lexian.manager.statistics.service;

import com.lexian.web.ResultHelper;

/**
 * @author Administrator
 */
public interface StatisticsService {

    ResultHelper getCommodityBrowseStatistics(Integer type, Integer size);

    ResultHelper getCommodityCollectionStatistics(Integer type, Integer size);

    ResultHelper getCommodityBuyStatistics(Integer type, Integer size);

    ResultHelper getStoreCommodityBuyStatistics(String storeNo, Integer type, Integer size);

    ResultHelper getStoreCommodityCollectionStatistics(String storeNo, Integer type, Integer size);

    ResultHelper getCommodityCollectionCount(Integer type);

    ResultHelper getCommodityBuyCount(Integer type);

    ResultHelper getCommodityBrowseCount(Integer type);

    ResultHelper getStoreCommodityCollectionCount(String storeNo, Integer type);

    ResultHelper getStoreCommodityBuyCount(String storeNo, Integer type);

}
