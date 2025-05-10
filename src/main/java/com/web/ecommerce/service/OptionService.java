package com.web.ecommerce.service;

import com.web.ecommerce.entity.Option;

public interface OptionService extends BaseService<Option, Integer> {
    Option findByName(String name);
}
