package com.lexian.manager.goods.service;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;


/**
 * @author Administrator
 */
public interface CommodityService {

    ResultHelper getCommodities(Page page);

    ResultHelper getCommodityByCategoryId(int categoryId);

    ResultHelper getCommodityByCommodityNo(String commodityNo);

    ResultHelper updateCommodity(Commodity commodity);

    ResultHelper addCommodity(Commodity commodity);

    ResultHelper getCommodityById(int id);

    ResultHelper deleteCommodityPictrue(String commodityNo);

    ResultHelper updateCommodityPicture(String commodityNo, String pictureUrl);
}
