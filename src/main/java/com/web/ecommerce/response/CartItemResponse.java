package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.CartItem;

import lombok.Data;

@Data
public class CartItemResponse {
    private int id;

    @JsonProperty("cart_id")
	private int cartId;
	
    @JsonProperty("product_id")
	private int productId;
	
	private int quantity;
	
	@JsonProperty("product_detail")
	private ProductResponse productDetailResponse;

	public CartItemResponse() {

	}

	public CartItemResponse(CartItem entity) {
		this.id = entity.getId();
		this.cartId = entity.getCartId();
		this.productId = entity.getProductId();
		this.quantity = entity.getQuantity();
	}
	
	public CartItemResponse(CartItem entity,ProductResponse productDetailResponse) {
		this(entity);
		this.productDetailResponse = productDetailResponse;
	}

	public List<CartItemResponse> mapToList(List<CartItem> entities) {
		return entities.stream().map(x -> new CartItemResponse(x)).collect(Collectors.toList());
	}
}
