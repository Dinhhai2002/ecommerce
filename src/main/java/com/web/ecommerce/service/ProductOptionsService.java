package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.ProductOptions;

public interface ProductOptionsService extends BaseService<ProductOptions, Integer> {
    ProductOptions findByName(String name);
    List<ProductOptions> findByProductId(Integer productId);
}
