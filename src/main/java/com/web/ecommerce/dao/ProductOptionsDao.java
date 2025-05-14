package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.ProductOptions;

public interface ProductOptionsDao extends BaseDao<ProductOptions, Integer> {
    ProductOptions findByName(String name);
    List<ProductOptions> findByProductId(Integer productId);
}