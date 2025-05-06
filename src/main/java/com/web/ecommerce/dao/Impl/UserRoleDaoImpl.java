package com.web.ecommerce.dao.Impl;

import com.web.ecommerce.dao.UserRoleDao;
import com.web.ecommerce.entity.UserRole;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.List;

@Repository("UserRoleDao")
@Transactional
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole, Integer> implements UserRoleDao {
    public UserRoleDaoImpl() {
        super(UserRole.class);
    }

    @Override
    public List<UserRole> findByUserId(Integer userId) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root).where(builder.equal(root.get("userId"), userId));
        return getSession().createQuery(query).getResultList();
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer userId) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root.get("roleId")).where(builder.equal(root.get("userId"), userId));
        return getSession().createQuery(query).getResultList();
    }
}
