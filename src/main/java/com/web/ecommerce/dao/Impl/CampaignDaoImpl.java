package com.web.ecommerce.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CampaignDao;
import com.web.ecommerce.entity.Campaign;
import com.web.ecommerce.entity.Category;

@Repository("CampaignDao")
@Transactional
public class CampaignDaoImpl extends BaseDaoImpl<Campaign, Integer> implements CampaignDao {
    
	public CampaignDaoImpl() {
		super(Campaign.class);
	}

	@Override
	public Campaign findByName(String name) {
		CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Campaign> query = builder.createQuery(Campaign.class);
		Root<Campaign> root = query.from(Campaign.class);
		query.where(builder.equal(root.get("name"), name));
		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}
	
	@Override
	public List<Campaign> findActiveByIds(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return List.of();
		}
		
		Date currentDate = new Date();
		CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Campaign> query = builder.createQuery(Campaign.class);
		Root<Campaign> root = query.from(Campaign.class);
		
		Predicate inClause = root.get("id").in(ids);
		Predicate activeClause = builder.equal(root.get("isActive"), true);
		Predicate startDateClause = builder.lessThanOrEqualTo(root.get("startDate"), currentDate);
		Predicate endDateClause = builder.greaterThanOrEqualTo(root.get("endDate"), currentDate);
		Predicate statusClause = builder.equal(root.get("status"), 1);
		
		query.where(
			builder.and(
				inClause,
				activeClause,
				startDateClause,
				endDateClause,
				statusClause
			)
		);
		
		return this.getSession().createQuery(query).getResultList();
	}
} 