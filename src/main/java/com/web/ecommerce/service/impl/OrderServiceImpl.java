package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.OrderDao;
import com.web.ecommerce.entity.Order;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.service.OrderService;

@Service("OrderService")
@Transactional(rollbackFor = Error.class)
public class OrderServiceImpl extends BaseServiceImpl<Order, Integer> implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public Order findByName(String name) {
        return orderDao.findByName(name);
    }

	@Override
	public StoreProcedureListResult<Order> spGListOrder(int userId, String keySearch, int status, int paymentStatus,
			int paymentMethod, Pagination pagination) throws Exception {
		return orderDao.spGListOrder(userId,keySearch,status,paymentStatus,paymentMethod,pagination);
	}
}
