package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.OrderStatusLog;

public interface OrderStatusLogDao extends BaseDao<OrderStatusLog, Integer> {
    OrderStatusLog findByName(String name);

    List<OrderStatusLog> findByOrderId(int orderId);
}