package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CartItemDao;
import com.web.ecommerce.entity.CartItem;
import com.web.ecommerce.service.CartItemService;

@Service("CartItemService")
@Transactional(rollbackFor = Error.class)
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, Integer> implements CartItemService {
    @Autowired
    private CartItemDao cartItemDao;

    @Override
    public CartItem findByName(String name) {
        return cartItemDao.findByName(name);
    }

    @Override
    public List<CartItem> findByCartId(int cartId) {
        return cartItemDao.findByCartId(cartId);
    }
}
