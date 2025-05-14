package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.CampaignProduct;

public interface CampaignProductDao extends BaseDao<CampaignProduct, Integer> {
    List<CampaignProduct> findByProductId(Integer productId);
} 