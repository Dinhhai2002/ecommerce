package com.web.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.common.enums.PaymentMethodEnum;
import com.web.ecommerce.common.enums.PaymentStatusEnum;
import com.web.ecommerce.common.enums.StatusOrderEnum;
import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Order;
import com.web.ecommerce.entity.OrderDetail;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.request.ChangeStatusOrderRequest;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OrderDetailResponse;
import com.web.ecommerce.response.OrderResponse;
import com.web.ecommerce.response.ProductResponse;
import com.web.ecommerce.service.OrderDetailService;
import com.web.ecommerce.service.OrderService;
import com.web.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController  {
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public OrderDetailService orderDetailService;
	
	@Autowired
	private ProductService productService;
	
	
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
	
	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<OrderResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Lấy danh sách OrderDetail theo orderId
		List<OrderDetail> orderDetails = orderDetailService
				.findByOrderId(order.getId());

		// Lấy danh sách productDetailIds
		List<Integer> productDetailIds = orderDetails.stream().map(OrderDetail::getProductDetailId)
				.collect(Collectors.toList());

		// Lấy thông tin ProductDetail
		List<Product> productDetails = productService.findByIds(productDetailIds);
		Map<Integer, ProductResponse> productDetailMap = productDetails.stream()
				.collect(Collectors.toMap(Product::getId, pd -> new ProductResponse(pd)));

		// Tạo response với ProductDetail được map
		List<OrderDetailResponse> orderDetailsResponse = orderDetails.stream().map(orderDetail -> {
			ProductResponse productDetailResponse = productDetailMap.get(orderDetail.getProductDetailId());
			return new OrderDetailResponse(orderDetail, productDetailResponse);
		}).collect(Collectors.toList());

		response.setData(new OrderResponse(order, orderDetailsResponse));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OrderResponse>> changeStatus(@PathVariable("id") int id,
			@Valid @RequestBody ChangeStatusOrderRequest wrapper) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra trạng thái mới có hợp lệ không
		if (!StatusOrderEnum.isValidStatus(wrapper.getStatus())) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Invalid order status");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra logic chuyển trạng thái
		int currentStatus = order.getStatus();
		int newStatus = wrapper.getStatus();

		// Đơn hàng đã hoàn thành không thể thay đổi trạng thái
		if (currentStatus == StatusOrderEnum.DELIVERED.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Cannot change status of completed order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Đơn hàng đã hủy không thể thay đổi trạng thái
		if (currentStatus == StatusOrderEnum.CANCELLED.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Cannot change status of cancelled order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra luồng trạng thái hợp lệ
		if (!isValidStatusTransition(currentStatus, newStatus)) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Invalid status transition");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra và cập nhật trạng thái thanh toán theo trạng thái đơn hàng
		if (newStatus == StatusOrderEnum.DELIVERED.getValue()) {
			// Nếu chuyển sang hoàn thành, đơn hàng phải được thanh toán
			if (order.getPaymentMethod() == PaymentMethodEnum.COD.getValue()
					|| order.getPaymentMethod() == PaymentMethodEnum.STORE.getValue()) {
				// Nếu là COD hoặc thanh toán tại quầy, tự động cập nhật trạng thái thanh toán
				// thành công
				order.setPaymentStatus(PaymentStatusEnum.PAID.getValue());
			} else if (order.getPaymentStatus() != PaymentStatusEnum.PAID.getValue()) {
				// Nếu không phải COD/STORE và chưa thanh toán, không cho phép hoàn thành
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Cannot complete unpaid order");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} else if (newStatus == StatusOrderEnum.CANCELLED.getValue()) {
			// Nếu hủy đơn hàng, cập nhật trạng thái thanh toán thành CANCELLED
			if (order.getPaymentStatus() == PaymentStatusEnum.PENDING.getValue()
					|| order.getPaymentStatus() == PaymentStatusEnum.PROCESSING.getValue()) {
				order.setPaymentStatus(PaymentStatusEnum.CANCELLED.getValue());
			}

			// Hoàn lại số lượng sản phẩm nếu đã trừ stock
			if (currentStatus == StatusOrderEnum.CONFIRMED.getValue()
					&& (order.getPaymentMethod() == PaymentMethodEnum.COD.getValue()
							|| order.getPaymentMethod() == PaymentMethodEnum.STORE.getValue())) {
				try {
					restoreProductStock(order.getId());
				} catch (Exception e) {
					// Log lỗi nhưng vẫn cho phép hủy đơn hàng
					System.err.println("Lỗi khi hoàn lại số lượng tồn kho: " + e.getMessage());
				}
			}
			if (order.getPaymentMethod() == PaymentMethodEnum.VNPAY.getValue()
					&& order.getStatus() == StatusOrderEnum.PROCESSING.getValue()) {
				try {
					restoreProductStock(order.getId());
				} catch (Exception e) {
					// Log lỗi nhưng vẫn cho phép hủy đơn hàng
					System.err.println("Lỗi khi hoàn lại số lượng tồn kho: " + e.getMessage());
				}
			}
		} else if (newStatus == StatusOrderEnum.CONFIRMED.getValue()) {
			// Cập nhật số lượng tồn kho khi xác nhận đơn hàng COD hoặc STORE
			if (order.getPaymentMethod() == PaymentMethodEnum.COD.getValue()
					|| order.getPaymentMethod() == PaymentMethodEnum.STORE.getValue()) {
				try {
					updateProductStock(order.getId());
				} catch (Exception e) {
					// Nếu không đủ hàng, trả về lỗi và không cho phép xác nhận đơn hàng
					response.setStatus(HttpStatus.BAD_REQUEST);
					response.setMessageError(e.getMessage());
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		}

		order.setStatus(newStatus);
		orderService.update(order);
		response.setData(new OrderResponse(order));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Kiểm tra luồng chuyển trạng thái có hợp lệ không
	private boolean isValidStatusTransition(int currentStatus, int newStatus) {
		// Từ PENDING có thể chuyển sang CONFIRMED, PROCESSING hoặc CANCELLED
		if (currentStatus == StatusOrderEnum.PENDING.getValue()) {
			return newStatus == StatusOrderEnum.CONFIRMED.getValue()
					|| newStatus == StatusOrderEnum.PROCESSING.getValue()
					|| newStatus == StatusOrderEnum.CANCELLED.getValue();
		}

		// Từ CONFIRMED có thể chuyển sang PROCESSING hoặc CANCELLED
		if (currentStatus == StatusOrderEnum.CONFIRMED.getValue()) {
			return newStatus == StatusOrderEnum.PROCESSING.getValue()
					|| newStatus == StatusOrderEnum.CANCELLED.getValue();
		}

		// Từ PROCESSING chỉ có thể chuyển sang SHIPPED hoặc CANCELLED
		if (currentStatus == StatusOrderEnum.PROCESSING.getValue()) {
			return newStatus == StatusOrderEnum.SHIPPED.getValue() || newStatus == StatusOrderEnum.CANCELLED.getValue();
		}

		// Từ SHIPPED chỉ có thể chuyển sang DELIVERED hoặc CANCELLED
		if (currentStatus == StatusOrderEnum.SHIPPED.getValue()) {
			return newStatus == StatusOrderEnum.DELIVERED.getValue()
					|| newStatus == StatusOrderEnum.CANCELLED.getValue();
		}

		return false;
	}
	
	private void updateProductStock(int orderId) throws Exception {
		List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);

		// Kiểm tra trước số lượng tồn kho có đủ hay không
		for (OrderDetail orderDetail : orderDetails) {
			Product productDetail = productService.findOne(orderDetail.getProductDetailId());
			if (productDetail == null) {
				throw new Exception("Không tìm thấy thông tin sản phẩm với ID: " + orderDetail.getProductDetailId());
			}
			
			if (productDetail.getStock() < orderDetail.getQuantity()) {
				throw new Exception("Sản phẩm " + productDetail.getName() + " không đủ số lượng tồn kho. Hiện chỉ còn " + productDetail.getStock());
			}
		}
		
		// Sau khi đã kiểm tra đủ số lượng, tiến hành cập nhật
		for (OrderDetail orderDetail : orderDetails) {
			Product productDetail = productService.findOne(orderDetail.getProductDetailId());
			productDetail.setStock(productDetail.getStock() - orderDetail.getQuantity());
			productService.update(productDetail);
		}
	}

	
	private void restoreProductStock(int orderId) throws Exception {
		List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);

		for (OrderDetail orderDetail : orderDetails) {
			Product productDetail = productService.findOne(orderDetail.getProductDetailId());
			if (productDetail != null) {
				productDetail.setStock(productDetail.getStock() + orderDetail.getQuantity());
				productService.update(productDetail);
			}
		}
	}
}
