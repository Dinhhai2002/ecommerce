package com.web.ecommerce.dao;

import com.web.ecommerce.entity.ProductImage;

public interface ProductImageDao extends BaseDao<ProductImage, Integer> {
    ProductImage findByName(String name);
}