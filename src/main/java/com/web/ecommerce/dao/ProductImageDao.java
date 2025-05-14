package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.ProductImage;

public interface ProductImageDao extends BaseDao<ProductImage, Integer> {
    ProductImage findByName(String name);
    List<ProductImage> findByProductId(Integer productId);
}