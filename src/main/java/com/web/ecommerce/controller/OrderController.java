package com.web.ecommerce.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
import com.web.ecommerce.common.utils.StringErrorValue;
import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.entity.Cart;
import com.web.ecommerce.entity.CartItem;
import com.web.ecommerce.entity.Order;
import com.web.ecommerce.entity.OrderDetail;
import com.web.ecommerce.entity.OrderStatusLog;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.entity.Users;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.request.CRUDOrderRequest;
import com.web.ecommerce.request.ChangePaymentStatusRequest;
import com.web.ecommerce.request.ChangeStatusOrderRequest;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OrderDetailResponse;
import com.web.ecommerce.response.OrderResponse;
import com.web.ecommerce.response.ProductResponse;
import com.web.ecommerce.security.ConfigVnpay;
import com.web.ecommerce.service.AddressBookService;
import com.web.ecommerce.service.CartItemService;
import com.web.ecommerce.service.CartService;
import com.web.ecommerce.service.OrderDetailService;
import com.web.ecommerce.service.OrderService;
import com.web.ecommerce.service.OrderStatusLogService;
import com.web.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController extends BaseUtilsController  {
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public OrderDetailService orderDetailService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressBookService addressBookService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;

	@Autowired
	private OrderStatusLogService orderStatusLogService;
	
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
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setOrderId(order.getId());
		orderStatusLog.setStatus(newStatus);
		orderStatusLog.setStatusName(StatusOrderEnum.getStatusName(newStatus));
		orderStatusLogService.create(orderStatusLog);
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
	
	@PostMapping("/create")
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody CRUDOrderRequest wrapper) throws Exception {
		BaseResponse response = new BaseResponse<>();
		Users currentUser = this.getUser();

		// Kiểm tra địa chỉ giao hàng
		AddressBook shippingAddress = addressBookService.findOne(wrapper.getAddressId());
		if (shippingAddress == null || shippingAddress.getUserId() != currentUser.getId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Address book not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Lấy cart và cart details của user
		List<Cart> carts = cartService.findByUserId(currentUser.getId());
		if (carts.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Cart cart = carts.get(0);
		List<CartItem> cartDetails = cartItemService.findByCartId(cart.getId());

		if (cartDetails.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Cart item not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra stock của sản phẩm
		List<Integer> productDetailIds = cartDetails.stream().map(CartItem::getProductId)
				.collect(Collectors.toList());

		List<Product> productDetails = productService.findByIds(productDetailIds);
		Map<Integer, Product> productDetailMap = productDetails.stream()
				.collect(Collectors.toMap(Product::getId, pd -> pd));

		// Kiểm tra số lượng tồn kho
		for (CartItem cartDetail : cartDetails) {
			Product productDetail = productDetailMap.get(cartDetail.getProductId());
			if (productDetail != null && cartDetail.getQuantity() > productDetail.getStock()) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Sản phẩm " + productDetail.getName()
						+ " vượt quá số lượng tồn kho. Số lượng tồn: " + productDetail.getStock());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		// Tạo order
		Order order = new Order();
		order.setUserId(currentUser.getId());
		order.setPrice(wrapper.getPrice());
		order.setDiscountAmount(wrapper.getDiscountAmount());
		order.setTotalPrice(wrapper.getTotalPrice());
		order.setPaymentMethod(wrapper.getPaymentMethod());
		order.setPaymentStatus(PaymentStatusEnum.PENDING.getValue());
		order.setStatus(StatusOrderEnum.PENDING.getValue());

//		// Kiểm tra voucher nếu có
//		if (wrapper.getVoucherId() > 0) {
//			Voucher voucher = voucherService.findOne(wrapper.getVoucherId());
//
//			if (voucher == null) {
//				response.setStatus(HttpStatus.BAD_REQUEST);
//				response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			}
//
//			// Kiểm tra điều kiện voucher
//			if (!voucher.isCurrentDateInRange() || voucher.isNumberLimit()
//					|| wrapper.getTotalPrice().compareTo(voucher.getMinOrderValue()) < 0) {
//				response.setStatus(HttpStatus.BAD_REQUEST);
//				response.setMessageError(StringErrorValue.VOUCHER_IS_NOT_APPLY);
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			}
//
//			// Cập nhật số lần sử dụng voucher
//			voucher.setUsedCount(voucher.getUsedCount() + 1);
//			voucherService.update(voucher);
//
//			// Áp dụng giảm giá vào đơn hàng
//
//			order.setVoucherId(wrapper.getVoucherId());
//		} else {
//			order.setVoucherId(0);
//		}
		order.setDiscountAmount(wrapper.getDiscountAmount());
		order.setTotalPrice(wrapper.getTotalPrice());

		// Thêm thông tin địa chỉ giao hàng
		order.setAddressId(shippingAddress.getId());
		order.setShippingName(shippingAddress.getFullName());
		order.setShippingPhone(shippingAddress.getPhone());
		order.setShippingWardId(shippingAddress.getWardId());
		order.setShippingWardName(shippingAddress.getWardName());
		order.setShippingDistrictId(shippingAddress.getDistrictId());
		order.setShippingDistrictName(shippingAddress.getDistrictName());
		order.setShippingCityId(shippingAddress.getCityId());
		order.setShippingCityName(shippingAddress.getCityName());
		order.setShippingAddress(shippingAddress.getFullAddress());
		order.setAmountShipping(wrapper.getAmountShipping());

		orderService.create(order);

		// Tạo order details
		for (CartItem cartDetail : cartDetails) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderId(order.getId());
			orderDetail.setProductDetailId(cartDetail.getProductId());
			orderDetail.setQuantity(cartDetail.getQuantity());

			Product productDetail = productDetailMap.get(cartDetail.getProductId());
			if (productDetail != null) {
				orderDetail.setPrice(productDetail.getPrice());
				BigDecimal totalPrice = productDetail.getPrice().multiply(new BigDecimal(cartDetail.getQuantity()));
				orderDetail.setTotalPrice(totalPrice);
			}

			orderDetail.setStatus(1);
			orderDetailService.create(orderDetail);

			// Xóa cart detail
			cartItemService.delete(cartDetail);
		}

		// Chỉ tạo URL thanh toán VNPAY nếu phương thức thanh toán là VNPAY
		if (wrapper.getPaymentMethod() == PaymentMethodEnum.VNPAY.getValue()) {
			String paymentUrl = generateVnPayUrl(wrapper.getTotalPrice(), String.valueOf(order.getId()));
			response.setData(paymentUrl);
		} else {
			// Nếu là COD hoặc thanh toán tại quầy, trả về thông tin đơn hàng
			response.setData(new OrderResponse(order));
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/payment-confirm/{id}")
	public ResponseEntity<BaseResponse> getPaymentUrl(@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse<>();
		Users users = this.getUser();

		// Kiểm tra đơn hàng tồn tại và thuộc về user hiện tại
		Order order = orderService.findOne(id);
		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("order not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra đơn hàng có phải của user hiện tại không
		if (order.getUserId() != users.getId()) {
			response.setStatus(HttpStatus.FORBIDDEN);
			response.setMessageError("You don't have permission to access this order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra trạng thái thanh toán của đơn hàng
		if (order.getPaymentStatus() != PaymentStatusEnum.PENDING.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Order has been paid or cancelled");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		String paymentUrl = generateVnPayUrl(order.getTotalPrice(), String.valueOf(order.getId()));
		response.setData(paymentUrl);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-payment-status")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OrderResponse>> changePaymentStatus(@PathVariable("id") int id,
			@Valid @RequestBody ChangePaymentStatusRequest wrapper) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra trạng thái thanh toán hợp lệ
		if (!PaymentStatusEnum.isValidStatus(wrapper.getPaymentStatus())) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Invalid payment status");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Cập nhật trạng thái thanh toán
		order.setPaymentStatus(wrapper.getPaymentStatus());

		// Nếu đã thanh toán thành công
		if (wrapper.getPaymentStatus() == PaymentStatusEnum.PAID.getValue()) {
			// Cập nhật trạng thái đơn hàng sang PROCESSING
			order.setStatus(StatusOrderEnum.PROCESSING.getValue());

			// Cập nhật số lượng tồn kho cho đơn hàng VNPAY
			if (order.getPaymentMethod() == PaymentMethodEnum.VNPAY.getValue()) {
				try {
					updateProductStock(order.getId());
				} catch (Exception e) {
					// Nếu không đủ hàng, trả về lỗi và không cho phép xác nhận thanh toán
					response.setStatus(HttpStatus.BAD_REQUEST);
					response.setMessageError(e.getMessage());
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		}
		// Nếu thanh toán thất bại hoặc bị hủy
		else if (wrapper.getPaymentStatus() == PaymentStatusEnum.FAILED.getValue()
				|| wrapper.getPaymentStatus() == PaymentStatusEnum.CANCELLED.getValue()) {
			// Cập nhật trạng thái đơn hàng sang CANCELLED
			order.setStatus(StatusOrderEnum.CANCELLED.getValue());
		}

		orderService.update(order);

		// Lấy thông tin chi tiết đơn hàng để trả về
		List<OrderDetail> orderDetails = orderDetailService.findByOrderId(id);
		List<OrderDetailResponse> orderDetailsResponse = new OrderDetailResponse().mapToList(orderDetails);

		response.setData(new OrderResponse(order, orderDetailsResponse));
		return new ResponseEntity<>(response, HttpStatus.OK);
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
	
	private String generateVnPayUrl(BigDecimal amount, String orderId) throws Exception {
		String vnp_TxnRef = ConfigVnpay.getRandomNumber(8);
		String vnp_TmnCode = applicationProperties.getVnpTmnCode();
		String ReturnUrl = applicationProperties.getBaseUrlFe() + "/payment-success";

		Map<String, String> vnp_Params = new Hashtable<>();
		vnp_Params.put("vnp_Version", ConfigVnpay.vnp_Version);
		vnp_Params.put("vnp_Command", ConfigVnpay.vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_BankCode", "NCB");
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
		vnp_Params.put("vnp_OrderType", "other");
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", ReturnUrl);
		vnp_Params.put("vnp_IpAddr", "13.160.92.202");

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = ConfigVnpay.hmacSHA512(applicationProperties.getVnpaySecretKey(), hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		return applicationProperties.getVnpPayUrl() + "?" + queryUrl;
	}
}
