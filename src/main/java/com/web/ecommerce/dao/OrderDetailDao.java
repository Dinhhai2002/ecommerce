package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.OrderDetail;

public interface OrderDetailDao extends BaseDao<OrderDetail, Integer> {
    OrderDetail findByName(String name);

    List<OrderDetail> findByOrderId(int orderId);
}