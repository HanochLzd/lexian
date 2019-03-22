package com.lexian.manager.plate.service;

import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

public interface SpeCommodityService {

    ResultHelper deleteSpeCommodity(int id);

    ResultHelper getSpecialCommodities(int id, Page page);

    ResultHelper addSpecialCommodities(String commodityNo, int id);
}
