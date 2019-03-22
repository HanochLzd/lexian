package com.lexian.manager.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.shop.bean.Citys;

public interface CitysDao {
	
	List<Citys> getCitiesByParentId(@Param("parentId") Integer parentId);
   
}
