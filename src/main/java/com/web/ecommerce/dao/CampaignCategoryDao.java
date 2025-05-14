package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.CampaignCategory;

public interface CampaignCategoryDao extends BaseDao<CampaignCategory, Integer> {
    List<CampaignCategory> findByCategoryId(Integer categoryId);
} 