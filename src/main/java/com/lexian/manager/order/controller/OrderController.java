package com.lexian.manager.order.controller;

import javax.validation.Valid;

import com.lexian.manager.order.service.impl.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.order.bean.Orders;
import com.lexian.manager.order.service.OrdersService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;


/**
 * @author luozidong
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrdersServiceImpl ordersService;


    @ResponseBody
    @RequestMapping("getOrderss.do")
    public ResultHelper getOrderss(Page page, Integer state) {

        return ordersService.getOrderss(page, state);
    }


    @ResponseBody
    @RequestMapping("getOrderDetail.do")
    public ResultHelper getOrderDetail(int id) {

        return ordersService.getOrderDetail(id);
    }

    @ResponseBody
    @RequestMapping("updateOrders.do")
    public ResultHelper updateOrders(@Valid Orders orders) {

        return ordersService.updateOrders(orders);
    }

    @ResponseBody
    @RequestMapping("getOrderssByDate.do")
    public ResultHelper getOrderssByDate(Integer state, String start, String end, Page page) {

        return ordersService.getOrderssByDate(state, start, end, page);
    }

}
