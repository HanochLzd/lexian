package com.lexian.manager.plate.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.plate.bean.SpecialCommodity;


/**
 * @author Administrator
 */
public interface SpeCommodityDao {

    void deleteSpeCommodity(int id);

    int getCountSpeCommodities(int id);

    List<SpecialCommodity> getSpecialCommoditiesPage(Map<String, Object> params);

    void addSpecialCommodities(@Param("commodityNo") String commodityNo, @Param("id") int id);

    SpecialCommodity getSpecialCommodity(@Param("commodityNo") String commodityNo, @Param("specialId") int specialId);

}
