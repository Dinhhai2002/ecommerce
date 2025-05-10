package com.web.ecommerce.dao.Impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.OptionDao;
import com.web.ecommerce.entity.Option;

@Repository("OptionDao")
@Transactional
public class OptionDaoImpl extends BaseDaoImpl<Option, Integer> implements OptionDao {
    public OptionDaoImpl() {
        super(Option.class);
    }

    @Override
    public Option findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Option> query = builder.createQuery(Option.class);
        Root<Option> root = query.from(Option.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
