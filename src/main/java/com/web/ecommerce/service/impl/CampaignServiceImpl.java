package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.CampaignDao;
import com.web.ecommerce.entity.Campaign;
import com.web.ecommerce.service.CampaignService;

@Service("CampaignService")
@Transactional(rollbackFor = Error.class)
public class CampaignServiceImpl extends BaseServiceImpl<Campaign, Integer> implements CampaignService {
    
    @Autowired
    private CampaignDao campaignDao;
    
    @Override
    public Campaign findByName(String name) {
        return campaignDao.findByName(name);
    }
    
    @Override
    public List<Campaign> findActiveByIds(List<Integer> ids) {
        return campaignDao.findActiveByIds(ids);
    }
} 