package com.web.ecommerce.service;

import com.web.ecommerce.entity.Cart;
import java.util.List;

public interface CartService extends BaseService<Cart, Integer> {
    Cart findByName(String name);
    List<Cart> findByUserId(int userId);
}
