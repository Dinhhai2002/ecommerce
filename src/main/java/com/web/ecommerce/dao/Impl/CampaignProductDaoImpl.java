package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CampaignProductDao;
import com.web.ecommerce.entity.CampaignProduct;

@Repository("CampaignProductDao")
@Transactional
public class CampaignProductDaoImpl extends BaseDaoImpl<CampaignProduct, Integer> implements CampaignProductDao {
    
    public CampaignProductDaoImpl() {
        super(CampaignProduct.class);
    }
    
    @Override
    public List<CampaignProduct> findByProductId(Integer productId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<CampaignProduct> query = builder.createQuery(CampaignProduct.class);
        Root<CampaignProduct> root = query.from(CampaignProduct.class);
        query.where(
            builder.and(
                builder.equal(root.get("productId"), productId),
                builder.equal(root.get("status"), 1)
            )
        );
        return this.getSession().createQuery(query).getResultList();
    }
}