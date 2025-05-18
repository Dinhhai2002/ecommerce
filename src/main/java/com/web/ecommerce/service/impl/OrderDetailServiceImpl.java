package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.OrderDetailDao;
import com.web.ecommerce.entity.OrderDetail;
import com.web.ecommerce.service.OrderDetailService;

@Service("OrderDetailService")
@Transactional(rollbackFor = Error.class)
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetail, Integer> implements OrderDetailService {
    @Autowired
    private OrderDetailDao orderDetailDao;

    @Override
    public OrderDetail findByName(String name) {
        return orderDetailDao.findByName(name);
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        return orderDetailDao.findByOrderId(orderId);
    }
}
