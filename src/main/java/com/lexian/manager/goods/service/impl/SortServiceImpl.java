package com.lexian.manager.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lexian.manager.goods.bean.Category;
import com.lexian.manager.goods.dao.SortDao;
import com.lexian.manager.goods.service.SortService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class SortServiceImpl implements SortService {

    @Resource
    private SortDao sortDao;

    @Override
    public ResultHelper getAllCategories(Page page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<Category> orderssWithStore = sortDao.getAllCategoriesPage(params);
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper getCategoryByCategoryName(String categoryName) {

        Category category = sortDao.getCategoryByCategoryName(categoryName);

        return new ResultHelper(Constant.CODE_SUCCESS, category);
    }

    @Override
    public ResultHelper updateCategoryById(Category category) {
        Category cate = new Category();
        if (category.getType() == 1) {
            cate = sortDao.getCountCategory(category.getCategoryName(), 1);
        } else {
            cate = sortDao.getCategory(category.getCategoryName(),
                    category.getType(), category.getParentId());
        }
        if (cate != null) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            sortDao.updateCategoryById(category.getId(), category.getCategoryName());
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }

    @Override
    public ResultHelper addCategory(Category category) {
        if (category.getType() == 1) {
            System.out.println("123456");
            Category cate = sortDao.getCountCategory(category.getCategoryName(), category.getType());
            if (cate != null) {
                return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
            } else {
                sortDao.addCategory(category);
                return new ResultHelper(Constant.CODE_SUCCESS);
            }
        } else {
            Category cate = sortDao.getCategory(category.getCategoryName(), category.getType(), category.getParentId());
            if (cate != null) {
                return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
            } else {
                sortDao.addCategory(category);
                return new ResultHelper(Constant.CODE_SUCCESS);
            }
        }
    }

    @Override
    public ResultHelper deleteCategory(int id) {
        if (sortDao.getCountCategoryByParentId(id) != 0) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            sortDao.deleteCategory(id);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }

    @Override
    public ResultHelper getFirstCategoryList(Page page) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<Category> aFirstCategory = sortDao.getFirstCategoryList(params);
        page.setData(aFirstCategory);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper getCategoryView(Page page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<Category> totalCategory = sortDao.getAllCategoriesPage(params);
        page.setData(totalCategory);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper getCategories() {
        List<Category> list = sortDao.getCategories();
        return new ResultHelper(Constant.CODE_SUCCESS, list);
    }

}
