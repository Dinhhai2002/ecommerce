package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CampaignProductDao;
import com.web.ecommerce.entity.CampaignProduct;
import com.web.ecommerce.service.CampaignProductService;

@Service("CampaignProductService")
@Transactional(rollbackFor = Error.class)
public class CampaignProductServiceImpl extends BaseServiceImpl<CampaignProduct, Integer> implements CampaignProductService {
    
    @Autowired
    private CampaignProductDao campaignProductDao;
    
    @Override
    public List<CampaignProduct> findByProductId(Integer productId) {
        return campaignProductDao.findByProductId(productId);
    }
} 