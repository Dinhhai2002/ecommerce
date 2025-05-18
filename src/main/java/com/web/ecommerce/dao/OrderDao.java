package com.web.ecommerce.dao;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Order;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface OrderDao extends BaseDao<Order, Integer> {
    Order findByName(String name);

	StoreProcedureListResult<Order> spGListOrder(int userId, String keySearch, int status, int paymentStatus,
			int paymentMethod, Pagination pagination) throws Exception;
}