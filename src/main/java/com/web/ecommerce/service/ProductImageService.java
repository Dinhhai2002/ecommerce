package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.ProductImage;

public interface ProductImageService extends BaseService<ProductImage, Integer> {
    ProductImage findByName(String name);
    List<ProductImage> findByProductId(Integer productId);
}
