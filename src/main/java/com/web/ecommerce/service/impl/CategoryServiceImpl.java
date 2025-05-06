package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CategoryDao;
import com.web.ecommerce.entity.Category;
import com.web.ecommerce.service.CategoryService;

/**
 * 
 * @author 
 *
 */
@Service
@Transactional(rollbackFor = Error.class)
public class CategoryServiceImpl extends BaseServiceImpl<Category, Integer> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Category findByName(String name) {
        return categoryDao.findByName(name);
    }
}
