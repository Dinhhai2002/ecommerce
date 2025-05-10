package com.web.ecommerce.service;

import com.web.ecommerce.entity.ProductImage;

public interface ProductImageService extends BaseService<ProductImage, Integer> {
    ProductImage findByName(String name);
}
