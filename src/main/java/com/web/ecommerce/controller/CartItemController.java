package com.web.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.CartItem;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.request.CRUDCartItemRequest;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.CartItemResponse;
import com.web.ecommerce.response.ProductResponse;
import com.web.ecommerce.service.CartItemService;
import com.web.ecommerce.service.ProductService;
import com.web.ecommerce.common.utils.StringErrorValue;

@RestController
@RequestMapping("/api/v1/cart-items")
public class CartItemController  {
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ProductService productService;
	
	/**
	 * Thêm sản phẩm vào giỏ hàng
	 * @param request Thông tin sản phẩm cần thêm
	 * @return Thông tin chi tiết item trong giỏ hàng
	 * @throws Exception
	 */
	@PostMapping("/create")
	public ResponseEntity<BaseResponse<CartItemResponse>> create(@RequestBody CRUDCartItemRequest request)
			throws Exception {
		BaseResponse<CartItemResponse> response = new BaseResponse<>();
		
		// Kiểm tra sản phẩm tồn tại
		Product product = productService.findOne(request.getProductId());
		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra số lượng tồn kho
		if (request.getQuantity() > product.getStock()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("số lượng tồn kho không đủ");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra sản phẩm đã có trong giỏ hàng hay chưa
		List<CartItem> existingItems = cartItemService.findByCartId(request.getCartId());
		CartItem existingItem = existingItems.stream()
				.filter(item -> item.getProductId() == request.getProductId())
				.findFirst()
				.orElse(null);
		
		// Nếu sản phẩm đã tồn tại trong giỏ hàng, cập nhật số lượng
		if (existingItem != null) {
			int newQuantity = existingItem.getQuantity() + request.getQuantity();
			
			// Kiểm tra lại số lượng sau khi cộng dồn
			if (newQuantity > product.getStock()) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("số lượng tồn kho không đủ");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			
			existingItem.setQuantity(newQuantity);
			cartItemService.update(existingItem);
			
			// Trả về thông tin item đã cập nhật
			response.setData(new CartItemResponse(existingItem, new ProductResponse(product)));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Nếu sản phẩm chưa có trong giỏ hàng, tạo mới cart item
		CartItem cartItem = new CartItem();
		cartItem.setCartId(request.getCartId());
		cartItem.setProductId(request.getProductId());
		cartItem.setQuantity(request.getQuantity());
		
		cartItemService.create(cartItem);
		
		// Trả về thông tin item đã tạo
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Cập nhật số lượng sản phẩm trong giỏ hàng
	 * @param id ID của cart item
	 * @param request Thông tin cập nhật
	 * @return Thông tin chi tiết item sau khi cập nhật
	 * @throws Exception
	 */
	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<CartItemResponse>> update(@PathVariable("id") int id,
			@RequestBody CRUDCartItemRequest request) throws Exception {
		BaseResponse<CartItemResponse> response = new BaseResponse<>();
		
		// Kiểm tra cart item tồn tại
		CartItem cartItem = cartItemService.findOne(id);
		if (cartItem == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy item trong giỏ hàng");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Nếu số lượng = 0, xóa item khỏi giỏ hàng
		if (request.getQuantity() == 0) {
			cartItemService.delete(cartItem);
			response.setStatus(HttpStatus.OK);
			response.setMessageError("Cart item deleted successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra sản phẩm tồn tại
		Product product = productService.findOne(cartItem.getProductId());
		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy sản phẩm trong giỏ hàng");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra số lượng tồn kho
		if (request.getQuantity() > product.getStock()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("số lượng tồn kho không đủ");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Cập nhật số lượng
		cartItem.setQuantity(request.getQuantity());
		cartItemService.update(cartItem);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Xóa sản phẩm khỏi giỏ hàng
	 * @param id ID của cart item
	 * @return Kết quả xóa
	 * @throws Exception
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<BaseResponse<String>> delete(@PathVariable("id") int id) throws Exception {
		BaseResponse<String> response = new BaseResponse<>();
		
		// Kiểm tra cart item tồn tại
		CartItem cartItem = cartItemService.findOne(id);
		if (cartItem == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy item trong giỏ hàng");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Xóa cart item
		cartItemService.delete(cartItem);
		
		// Trả về kết quả
		response.setData("Cart item deleted successfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<CartItemResponse>>> getAll(
			@RequestParam(name = "cart_id", required = false, defaultValue = "-1") int cartId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<CartItemResponse>> response = new BaseResponse<>();
		List<CartItem> listCartItem = cartItemService.findByCartId(cartId);
		Set<Integer> listProductDetailIds = listCartItem.stream().map(item -> item.getProductId())
				.collect(Collectors.toSet());

		List<Product> productDetails = productService.findByIds(new ArrayList<>(listProductDetailIds));

		Map<Integer, ProductResponse> productDetailResponseMap = productDetails.stream()
				.collect(Collectors.toMap(Product::getId, ProductResponse::new));

		BaseListDataResponse<CartItemResponse> listData = new BaseListDataResponse<>();

		// Sử dụng map để ánh xạ ProductDetailResponse vào CartDetailResponse
		listData.setList(listCartItem.stream().map(cartDetail -> {
			ProductResponse productDetailResponse = productDetailResponseMap.get(cartDetail.getProductId());
			return new CartItemResponse(cartDetail, productDetailResponse);
		}).collect(Collectors.toList()));

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
