package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.ProductDao;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.service.ProductService;

@Service("ProductService")
@Transactional(rollbackFor = Error.class)
public class ProductServiceImpl extends BaseServiceImpl<Product, Integer> implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public Product findByName(String name) {
        return productDao.findByName(name);
    }
}
