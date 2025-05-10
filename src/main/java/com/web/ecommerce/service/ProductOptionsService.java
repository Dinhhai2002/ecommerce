package com.web.ecommerce.service;

import com.web.ecommerce.entity.ProductOptions;

public interface ProductOptionsService extends BaseService<ProductOptions, Integer> {
    ProductOptions findByName(String name);
}
