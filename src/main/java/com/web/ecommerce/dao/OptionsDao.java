package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Options;

public interface OptionsDao extends BaseDao<Options, Integer> {
    Options findByName(String name);
}