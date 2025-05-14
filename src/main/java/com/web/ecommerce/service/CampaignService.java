package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.Campaign;

public interface CampaignService extends BaseService<Campaign, Integer> {
    Campaign findByName(String name);
    List<Campaign> findActiveByIds(List<Integer> ids);
} 