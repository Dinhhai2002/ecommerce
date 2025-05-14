package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CampaignCategoryDao;
import com.web.ecommerce.entity.CampaignCategory;
import com.web.ecommerce.service.CampaignCategoryService;

@Service("CampaignCategoryService")
@Transactional(rollbackFor = Error.class)
public class CampaignCategoryServiceImpl extends BaseServiceImpl<CampaignCategory, Integer> implements CampaignCategoryService {
    
    @Autowired
    private CampaignCategoryDao campaignCategoryDao;
    
    @Override
    public List<CampaignCategory> findByCategoryId(Integer categoryId) {
        return campaignCategoryDao.findByCategoryId(categoryId);
    }
} 