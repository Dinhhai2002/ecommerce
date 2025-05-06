package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Category;

public interface CategoryDao extends BaseDao<Category, Integer> {
    Category findByName(String name);
}
