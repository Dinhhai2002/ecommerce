package com.web.ecommerce.dao.Impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.ProductOptionsDao;
import com.web.ecommerce.entity.ProductOptions;

@Repository("ProductOptionsDao")
@Transactional
public class ProductOptionsDaoImpl extends BaseDaoImpl<ProductOptions, Integer> implements ProductOptionsDao {
    public ProductOptionsDaoImpl() {
        super(ProductOptions.class);
    }

    @Override
    public ProductOptions findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<ProductOptions> query = builder.createQuery(ProductOptions.class);
        Root<ProductOptions> root = query.from(ProductOptions.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
