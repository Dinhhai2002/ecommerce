package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.entity.Campaign;

public interface CampaignDao extends BaseDao<Campaign, Integer> {
    Campaign findByName(String name);
    List<Campaign> findActiveByIds(List<Integer> ids);
} 