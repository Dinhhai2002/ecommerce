package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.CartItem;

public interface CartItemService extends BaseService<CartItem, Integer> {
    CartItem findByName(String name);
    List<CartItem> findByCartId(int cartId);
}
