package com.web.ecommerce.dao;

import com.web.ecommerce.entity.OptionValues;

public interface OptionValuesDao extends BaseDao<OptionValues, Integer> {
    OptionValues findByName(String name);
}