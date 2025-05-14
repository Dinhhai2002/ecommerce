package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.OptionValuesDao;
import com.web.ecommerce.entity.OptionValues;

@Repository("OptionValuesDao")
@Transactional
public class OptionValuesDaoImpl extends BaseDaoImpl<OptionValues, Integer> implements OptionValuesDao {
    public OptionValuesDaoImpl() {
        super(OptionValues.class);
    }

    @Override
    public OptionValues findByName(String value) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OptionValues> query = builder.createQuery(OptionValues.class);
        Root<OptionValues> root = query.from(OptionValues.class);
        query.where(builder.equal(root.get("value"), value));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
    
    @Override
    public List<OptionValues> findByOptionId(Integer optionId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OptionValues> query = builder.createQuery(OptionValues.class);
        Root<OptionValues> root = query.from(OptionValues.class);
        query.where(
            builder.and(
                builder.equal(root.get("optionId"), optionId),
                builder.equal(root.get("status"), 1)
            )
        );
        return this.getSession().createQuery(query).getResultList();
    }
}
