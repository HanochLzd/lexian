package com.lexian.manager.goods.dao;

import java.util.List;
import java.util.Map;

import com.lexian.manager.goods.bean.CategoryView;

/**
 * @author luozidong
 */
public interface CategoryViewDao {

    List<CategoryView> getAllCategoryViewPage(Map<String, Object> params);

    List<CategoryView> getFirstCategoryViewPage(Map<String, Object> params);

    List<CategoryView> getSecondCategoryViewPage(Map<String, Object> params);

    List<CategoryView> getThirdCategoryViewPage(Map<String, Object> params);
	
	/*public int getCountCategory();
	
	 int getFirstCountCategory();
	
	 int getSecondCountCategory();
	
	 int getThirdCountCategory();*/
}
