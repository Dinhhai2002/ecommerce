package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.CampaignProduct;

public interface CampaignProductService extends BaseService<CampaignProduct, Integer> {
    List<CampaignProduct> findByProductId(Integer productId);
} 