package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.CartItem;

public interface CartItemDao extends BaseDao<CartItem, Integer> {
    CartItem findByName(String name);

    List<CartItem> findByCartId(int cartId);
}