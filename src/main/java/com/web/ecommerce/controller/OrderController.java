package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Order;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OrderResponse;
import com.web.ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController  {
	@Autowired
	public OrderService orderService;
	
	
	@GetMapping("")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OrderResponse>>> getAll(
			@RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "payment_status", required = false, defaultValue = "-1") int paymentStatus,
			@RequestParam(name = "payment_method", required = false, defaultValue = "-1") int paymentMethod,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<OrderResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Order> listOrder = orderService.spGListOrder(userId, keySearch, status, paymentStatus, paymentMethod, pagination);

		BaseListDataResponse<OrderResponse> listData = new BaseListDataResponse<>();

		listData.setList(new OrderResponse().mapToList(listOrder.getResult()));
		listData.setTotalRecord(listOrder.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
