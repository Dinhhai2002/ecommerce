package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Cart;
import java.util.List;

public interface CartDao extends BaseDao<Cart, Integer> {
    Cart findByName(String name);
    List<Cart> findByUserId(int userId);
}