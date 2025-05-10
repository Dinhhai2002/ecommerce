package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Option;

public interface OptionDao extends BaseDao<Option, Integer> {
    Option findByName(String name);
}