package com.lexian.manager.order.dao;

import java.util.List;
import java.util.Map;

import com.lexian.manager.order.bean.Orders;


/**
 * @author Administrator
 */
public interface OrdersDao {

    List<Orders> getOrderssWithStorePage(Map<String, Object> params);

    Orders getOrdersWithUserAndOrderItemsStore(Integer id);

    void updateOrders(Orders orders);

    List<Orders> getOrderssByDatePage(Map<String, Object> params);

}
