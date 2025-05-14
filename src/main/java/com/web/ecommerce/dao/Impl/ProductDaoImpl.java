package com.web.ecommerce.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.ProductDao;
import com.web.ecommerce.entity.Product;

@Repository("ProductDao")
@Transactional
public class ProductDaoImpl extends BaseDaoImpl<Product, Integer> implements ProductDao {
    public ProductDaoImpl() {
        super(Product.class);
    }

    @Override
    public Product findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
