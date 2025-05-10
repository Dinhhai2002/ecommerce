package com.web.ecommerce.dao.Impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.OptionsDao;
import com.web.ecommerce.entity.Options;

@Repository("OptionsDao")
@Transactional
public class OptionsDaoImpl extends BaseDaoImpl<Options, Integer> implements OptionsDao {
    public OptionsDaoImpl() {
        super(Options.class);
    }

    @Override
    public Options findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Options> query = builder.createQuery(Options.class);
        Root<Options> root = query.from(Options.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
