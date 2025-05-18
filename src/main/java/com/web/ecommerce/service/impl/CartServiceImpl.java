package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CartDao;
import com.web.ecommerce.entity.Cart;
import com.web.ecommerce.service.CartService;

@Service("CartService")
@Transactional(rollbackFor = Error.class)
public class CartServiceImpl extends BaseServiceImpl<Cart, Integer> implements CartService {
    @Autowired
    private CartDao cartDao;

    @Override
    public Cart findByName(String name) {
        return cartDao.findByName(name);
    }

    @Override
    public List<Cart> findByUserId(int userId) {
        return cartDao.findByUserId(userId);
    }       
}
