package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CRUDCartItemRequest {

	@NotEmpty(message = "cart_id không được phép để trống")
	private int cartId;

	@NotEmpty(message = "product_id không được phép để trống")
	private int productId;

	@NotEmpty(message = "quantity không được phép để trống")
	private int quantity;
}

