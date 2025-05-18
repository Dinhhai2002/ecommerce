package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.OrderDetail;

public interface OrderDetailService extends BaseService<OrderDetail, Integer> {
    OrderDetail findByName(String name);

    List<OrderDetail> findByOrderId(int orderId);
}
