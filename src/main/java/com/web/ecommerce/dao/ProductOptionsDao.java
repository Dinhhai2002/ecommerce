package com.web.ecommerce.dao;

import com.web.ecommerce.entity.ProductOptions;

public interface ProductOptionsDao extends BaseDao<ProductOptions, Integer> {
    ProductOptions findByName(String name);
}