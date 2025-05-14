package com.web.ecommerce.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CategoryDao;
import com.web.ecommerce.entity.Category;

@Repository("CategoryDao")
@Transactional
public class CategoryDaoImpl extends BaseDaoImpl<Category, Integer> implements CategoryDao {
	public CategoryDaoImpl() {
		super(Category.class);
	}

	@Override
	public Category findByName(String name) {
		CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Category> query = builder.createQuery(Category.class);
		Root<Category> root = query.from(Category.class);
		query.where(builder.equal(root.get("name"), name));
		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}
}
