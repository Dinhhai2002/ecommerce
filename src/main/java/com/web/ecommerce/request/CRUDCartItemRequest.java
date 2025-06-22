package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDCartItemRequest {

	@NotEmpty(message = "cart_id không được phép để trống")
	@JsonProperty("cart_id")
	private int cartId;

	@NotEmpty(message = "product_id không được phép để trống")
	@JsonProperty("product_id")
	private int productId;

	@NotEmpty(message = "quantity không được phép để trống")
	private int quantity;
}

