package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.ProductOptionsDao;
import com.web.ecommerce.entity.ProductOptions;
import com.web.ecommerce.service.ProductOptionsService;

@Service("ProductOptionsService")
@Transactional(rollbackFor = Error.class)
public class ProductOptionsServiceImpl extends BaseServiceImpl<ProductOptions, Integer> implements ProductOptionsService {
    @Autowired
    private ProductOptionsDao productOptionsDao;

    @Override
    public ProductOptions findByName(String name) {
        return productOptionsDao.findByName(name);
    }

    @Override
    public List<ProductOptions> findByProductId(Integer productId) {
        return productOptionsDao.findByProductId(productId);
    }
}
