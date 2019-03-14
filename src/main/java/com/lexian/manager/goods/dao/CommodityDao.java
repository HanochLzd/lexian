package com.lexian.manager.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.manager.goods.bean.CommoditySpec;

/**
 * @author luozidong
 */
public interface CommodityDao {

    List<Commodity> getCommoditiesPage(Map<String, Object> params);

    List<Commodity> getCommodityByCategoryId(int categoryId);

    Commodity getCommodityByCommodityNo(String commodityNo);

    Commodity getCommodityById(int id);

    void updateCommodity(Commodity commodity);

    void addCommodity(Commodity commodity);

    void addCommoditySpec(CommoditySpec commoditySpec);

    void deleteCommoditySpec(String commodityNo);

    void addCommodityPicture(@Param("commodityNo") String commodityNo, @Param("pictureUrl") String pictureUrl);

    void deleteCommodityPicture(@Param("commodityNo") String commodityNo);
}
