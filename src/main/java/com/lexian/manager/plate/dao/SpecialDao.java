package com.lexian.manager.plate.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.plate.bean.Special;


/**
 * @author Administrator
 */
public interface SpecialDao {

    List<Special> getSpecialPage(Map<String, Object> params);

    void updateSpecial(@Param("id") int id, @Param("name") String name);

    void deleteSpecial(int id);

    void addSpecial(String name);

    Special getSpecialByName(String name);

    int getCountSpecial();
}
