package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.OptionValues;

public interface OptionValuesDao extends BaseDao<OptionValues, Integer> {
    OptionValues findByName(String name);
    List<OptionValues> findByOptionId(Integer optionId);
}