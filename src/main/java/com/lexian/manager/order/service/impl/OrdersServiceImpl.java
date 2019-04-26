package com.lexian.manager.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexian.cache.annotation.Cacheable;
import com.lexian.utils.UrlConstant;
import org.springframework.stereotype.Service;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.manager.order.bean.OrderItem;
import com.lexian.manager.order.bean.Orders;
import com.lexian.manager.order.dao.OrdersDao;
import com.lexian.manager.order.service.OrdersService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class OrdersServiceImpl {

    @Resource
    private OrdersDao ordersDao;


    @Cacheable
    public ResultHelper getOrderss(Page page, Integer state) {

        Map<String, Object> params = new HashMap<>(2);
        params.put("page", page);

        if (state != null) {
            params.put("state", state);
        }

        List<Orders> orderssWithStore = ordersDao.getOrderssWithStorePage(params);
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }


    public ResultHelper getOrderDetail(int id) {
        Orders orders = ordersDao.getOrdersWithUserAndOrderItemsStore(id);

        for (OrderItem item : orders.getOrderItems()) {
            Commodity commodity = item.getCommodity();
            commodity.setPictureUrl(UrlConstant.QI_NIU_URL + "/" + commodity.getPictureUrl());
        }

        return new ResultHelper(Constant.CODE_SUCCESS, orders);
    }


    public ResultHelper updateOrders(Orders orders) {

        ordersDao.updateOrders(orders);

        return new ResultHelper(Constant.CODE_SUCCESS);
    }


    public ResultHelper getOrderssByDate(Integer state, String start, String end, Page page) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);

        if (state != null) {
            params.put("state", state);
        }

        params.put("start", start);
        params.put("end", end);
        List<Orders> orderssWithStore = ordersDao.getOrderssByDatePage(params);
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

}
