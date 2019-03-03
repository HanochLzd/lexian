/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.manager.order.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.order.bean.Orders;
import com.lexian.manager.order.service.OrdersService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * 
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 郝伟
 * @version 1.0
 */
@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrdersService ordersService;

	// order/getOrderss.do?pageNo=1
	@ResponseBody
	@RequestMapping("getOrderss.do")
	public ResultHelper getOrderss(Page page,Integer state) {

		return ordersService.getOrderss(page,state);
	}


	// order/getOrderDetail.do?id=4
	@ResponseBody
	@RequestMapping("getOrderDetail.do")
	public ResultHelper getOrderDetail(int id) {

		return ordersService.getOrderDetail(id);
	}

	// order/updateOrders.do?id=4&states=3
	@ResponseBody
	@RequestMapping("updateOrders.do")
	public ResultHelper updateOrders(@Valid Orders orders) {

		return ordersService.updateOrders(orders);
	}
	
	// order/getOrderssByDate.do?state=2&pageNo=1&start=2016-7-5&end=2016-7-8
	@ResponseBody
	@RequestMapping("getOrderssByDate.do")
	public ResultHelper getOrderssByDate(Integer state,String start,String end,Page page) {

		return ordersService.getOrderssByDate(state,start,end,page);
	}
	
}
