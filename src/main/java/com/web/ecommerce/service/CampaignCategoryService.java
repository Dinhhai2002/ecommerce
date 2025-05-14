package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.CampaignCategory;

public interface CampaignCategoryService extends BaseService<CampaignCategory, Integer> {
    List<CampaignCategory> findByCategoryId(Integer categoryId);
} 