package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.ProductImageDao;
import com.web.ecommerce.entity.ProductImage;

@Repository("ProductImageDao")
@Transactional
public class ProductImageDaoImpl extends BaseDaoImpl<ProductImage, Integer> implements ProductImageDao {
    
    public ProductImageDaoImpl() {
        super(ProductImage.class);
    }
    
    @Override
    public ProductImage findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<ProductImage> query = builder.createQuery(ProductImage.class);
        Root<ProductImage> root = query.from(ProductImage.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
    
    @Override
    public List<ProductImage> findByProductId(Integer productId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<ProductImage> query = builder.createQuery(ProductImage.class);
        Root<ProductImage> root = query.from(ProductImage.class);
        query.where(
            builder.and(
                builder.equal(root.get("productId"), productId),
                builder.equal(root.get("status"), 1)
            )
        );
        return this.getSession().createQuery(query).getResultList();
    }
}
