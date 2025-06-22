package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.OrderStatusLog;

public interface OrderStatusLogService extends BaseService<OrderStatusLog, Integer> {
    OrderStatusLog findByName(String name);

    List<OrderStatusLog> findByOrderId(int orderId);
}
