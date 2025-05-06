package com.web.ecommerce.service;

import com.web.ecommerce.entity.Category;

public interface CategoryService extends BaseService<Category, Integer> {
    Category findByName(String name);
}
