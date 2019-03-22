package com.lexian.manager.goods.service;

import com.lexian.manager.goods.bean.Category;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;


/**
 * @author Administrator
 */
public interface SortService {

    /**
     * 查询所有类别
     *
     * @return
     */
    ResultHelper getCategories();

    ResultHelper getAllCategories(Page page);

    ResultHelper getCategoryByCategoryName(String categoryName);

    ResultHelper updateCategoryById(Category category);

    ResultHelper addCategory(Category category);

    ResultHelper deleteCategory(int id);

    ResultHelper getFirstCategoryList(Page page);

    ResultHelper getCategoryView(Page page);
}
