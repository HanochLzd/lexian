/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.manager.shop.service;

import java.util.List;

import com.lexian.manager.shop.bean.CommodityStore;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * 
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 王子龙
 * @version 1.0
 */
public interface CommodityStoreService {
  
   public ResultHelper getCommodityByStoreNo(String storeNo, Page page);
   public ResultHelper updateCommodityStore(CommodityStore commodityStore);
   public ResultHelper addCommodityStore(List<CommodityStore> list);
   public ResultHelper getCommodityByCategoryId(int categoryId, String storeNo);
   public ResultHelper registCommodityStore(String storeNo, String[] commodityNo, Integer type);
}
 