package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.OrderDetail;
import com.web.ecommerce.response.OrderDetailResponse;

@RestController
@RequestMapping("/api/v1/orderDetail")
public class OrderDetailController extends BaseController<OrderDetail, OrderDetailResponse, Integer> {
    public OrderDetailController() {
        super(OrderDetailResponse::new);
    }
}
