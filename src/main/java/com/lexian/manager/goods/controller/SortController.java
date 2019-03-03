/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.manager.goods.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lexian.manager.goods.bean.Category;
import com.lexian.manager.goods.service.SortService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * 
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 陈浩
 * @version 1.0
 */
@Controller
@RequestMapping("sort")
@SessionAttributes(value={"managerId"},types={Integer.class})
public class SortController {
	@Autowired
	private SortService sortService;
	
	public SortService getSortService() {
		return sortService;
	}

	public void setSortService(SortService sortService) {
		this.sortService = sortService;
	}

	/**
	 * 查询所有的category
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getCategories.do")
	 public ResultHelper getCategories(){
		ResultHelper result=sortService.getCategories();
		return result;
		//sort/getCategories.do
	 }
	/**
	 * 
	 * @param pageNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getCategoryView.do")
	 public ResultHelper getCategoryView(Page page){
		ResultHelper result=sortService.getAllCategories(page);
		return result;
		//sort/getCategoryView.do
	 }
	/**
	 * 通过categoryName查询
	 * @param categoryName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getCategoryByCategoryName.do")
	 public ResultHelper getCategoryByCategoryName(String categoryName){
		ResultHelper result=sortService.getCategoryByCategoryName(categoryName);
		//sort/getCategoryByCategoryName.do?categoryName=学习装备
		return result;
		
	 }
	/**
	 * 添加category
	 * @param category
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addCategory.do")
	 public ResultHelper addCategory(@Valid Category category){
		//sort/addCategory.do
		return sortService.addCategory(category);
		 
	 }
	
	/**
	 * 修改category
	 * @param id
	 * @param categoryName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateCategory.do")
	 public ResultHelper updateCategoryById(@Valid Category category){
		//sort/updateCategory.do?id=46&categoryName=装备学习
		//sort/updateCategory.do?id=10&categoryName=学习装备2
		return sortService.updateCategoryById(category);
		 
	 }
	/**
	 * 删除category
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteCategory.do")
	 public ResultHelper deleteCategory(int id){
		 //sort/deleteCategory.do?id=10
		return sortService.deleteCategory(id);
	 }
}
