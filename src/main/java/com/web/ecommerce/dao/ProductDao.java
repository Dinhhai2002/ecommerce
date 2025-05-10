package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Product;

public interface ProductDao extends BaseDao<Product, Integer> {
    Product findByName(String name);
}