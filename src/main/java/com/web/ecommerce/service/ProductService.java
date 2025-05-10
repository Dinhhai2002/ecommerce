package com.web.ecommerce.service;

import com.web.ecommerce.entity.Product;

public interface ProductService extends BaseService<Product, Integer> {
    Product findByName(String name);
}
