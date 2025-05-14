package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.OptionValues;

public interface OptionValuesService extends BaseService<OptionValues, Integer> {
    OptionValues findByName(String name);
    List<OptionValues> findByOptionId(Integer optionId);
}
