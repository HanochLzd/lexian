package com.lexian.manager.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lexian.manager.goods.bean.CategoryView;
import com.lexian.manager.goods.dao.CategoryViewDao;
import com.lexian.manager.goods.service.CategoryViewService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class CategoryViewImpl implements CategoryViewService{

	@Resource
	private CategoryViewDao categoryViewDao;

	@Override
	public ResultHelper getAllCategoryView(Page page) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		List<CategoryView> orderssWithStore = categoryViewDao.getAllCategoryViewPage(params);
		page.setData(orderssWithStore);

		return new ResultHelper(Constant.CODE_SUCCESS, page);
	}

	@Override
	public ResultHelper getFirstCategoryView(Page page) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		List<CategoryView> orderssWithStore = categoryViewDao.getFirstCategoryViewPage(params);
		page.setData(orderssWithStore);

		return new ResultHelper(Constant.CODE_SUCCESS, page);
	}

	@Override
	public ResultHelper getSecondCategoryView(Page page) {

		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		List<CategoryView> orderssWithStore = categoryViewDao.getSecondCategoryViewPage(params);
		page.setData(orderssWithStore);

		return new ResultHelper(Constant.CODE_SUCCESS, page);
	}

	@Override
	public ResultHelper getThirdCategoryView(Page page) {
	
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		List<CategoryView> orderssWithStore = categoryViewDao.getThirdCategoryViewPage(params);
		page.setData(orderssWithStore);

		return new ResultHelper(Constant.CODE_SUCCESS, page);
	}

}
