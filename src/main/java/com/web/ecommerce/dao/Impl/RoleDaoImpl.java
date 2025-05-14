package com.web.ecommerce.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.RoleDao;
import com.web.ecommerce.entity.Role;

import java.util.List;

@Repository("RoleDao")
@Transactional
public class RoleDaoImpl extends BaseDaoImpl<Role, Integer> implements RoleDao {
    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public List<String> findRoleNamesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return java.util.Collections.emptyList();
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Role> root = query.from(Role.class);
        query.select(root.get("name")).where(root.get("id").in(ids));
        return this.getSession().createQuery(query).getResultList();
    }
}
