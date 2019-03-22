package com.lexian.manager.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lexian.manager.goods.bean.Category;

/**
 * @author Administrator
 */
public interface SortDao {

    List<Category> getAllCategoriesPage(Map<String, Object> params);

    Category getCategoryByCategoryName(String categoryName);

    void updateCategoryById(@Param("id") int id, @Param("categoryName") String categoryName);

    void addCategory(Category category);

    void deleteCategory(int id);

    Category getCategory(@Param("categoryName") String categoryName, @Param("type") int type,
                         @Param("parentId") Integer parentId);

    Category getCountCategory(@Param("categoryName") String categoryName, @Param("type") int type);

    int getCountCategoryByParentId(int parentId);

    List<Category> getCategories();

    List<Category> getFirstCategoryList(Map<String, Object> params);
}
