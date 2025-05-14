package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CampaignCategoryDao;
import com.web.ecommerce.entity.CampaignCategory;

@Repository("CampaignCategoryDao")
@Transactional
public class CampaignCategoryDaoImpl extends BaseDaoImpl<CampaignCategory, Integer> implements CampaignCategoryDao {
    
    public CampaignCategoryDaoImpl() {
        super(CampaignCategory.class);
    }
    
    @Override
    public List<CampaignCategory> findByCategoryId(Integer categoryId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<CampaignCategory> query = builder.createQuery(CampaignCategory.class);
        Root<CampaignCategory> root = query.from(CampaignCategory.class);
        query.where(
            builder.and(
                builder.equal(root.get("categoryId"), categoryId),
                builder.equal(root.get("status"), 1)
            )
        );
        return this.getSession().createQuery(query).getResultList();
    }
} 