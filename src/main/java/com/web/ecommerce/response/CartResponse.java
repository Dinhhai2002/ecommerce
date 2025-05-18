package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.Cart;

import lombok.Data;

@Data
public class CartResponse {
    private int id;

    @JsonProperty("user_id")
	private int userId;

	private int status;

	public CartResponse() {

	}

	public CartResponse(Cart entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.status = entity.getStatus();
	}

	public List<CartResponse> mapToList(List<Cart> entities) {
		return entities.stream().map(x -> new CartResponse(x)).collect(Collectors.toList());
	}
}
