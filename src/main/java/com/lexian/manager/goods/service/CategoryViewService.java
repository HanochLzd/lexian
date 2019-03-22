package com.lexian.manager.goods.service;

import com.lexian.web.Page;
import com.lexian.web.ResultHelper;


/**
 * @author Administrator
 */
public interface CategoryViewService {

	 ResultHelper getAllCategoryView(Page page);
	
	 ResultHelper getFirstCategoryView(Page page);
	
	 ResultHelper getSecondCategoryView(Page page);
	
	 ResultHelper getThirdCategoryView(Page page);
}
