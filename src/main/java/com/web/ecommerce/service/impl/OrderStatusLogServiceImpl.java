package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.OrderStatusLogDao;
import com.web.ecommerce.entity.OrderStatusLog;
import com.web.ecommerce.service.OrderStatusLogService;

@Service("OrderStatusLogService")
@Transactional(rollbackFor = Error.class)
public class OrderStatusLogServiceImpl extends BaseServiceImpl<OrderStatusLog, Integer> implements OrderStatusLogService {
    @Autowired
    private OrderStatusLogDao orderStatusLogDao;

    @Override
    public OrderStatusLog findByName(String name) {
        return orderStatusLogDao.findByName(name);
    }

    @Override
    public List<OrderStatusLog> findByOrderId(int orderId) {
        return orderStatusLogDao.findByOrderId(orderId);
    }
}
