package com.web.ecommerce.service;

import com.web.ecommerce.entity.Options;

public interface OptionsService extends BaseService<Options, Integer> {
    Options findByName(String name);
}
