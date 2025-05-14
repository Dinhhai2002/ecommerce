package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.ProductImageDao;
import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.service.ProductImageService;

@Service("ProductImageService")
@Transactional(rollbackFor = Error.class)
public class ProductImageServiceImpl extends BaseServiceImpl<ProductImage, Integer> implements ProductImageService {
    @Autowired
    private ProductImageDao productImageDao;

    @Override
    public ProductImage findByName(String name) {
        return productImageDao.findByName(name);
    }

    @Override
    public List<ProductImage> findByProductId(Integer productId) {
        return productImageDao.findByProductId(productId);
    }
}
