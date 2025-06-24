package com.web.ecommerce.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.ApiPermissionDao;
import com.web.ecommerce.entity.ApiPermission;

@Repository("ApiPermissionDao")
@Transactional
public class ApiPermissionDaoImpl extends BaseDaoImpl<ApiPermission, Integer> implements ApiPermissionDao {

    public ApiPermissionDaoImpl() {
        super(ApiPermission.class);
    }

	@Override
    public Optional<ApiPermission> findByApiPathAndHttpMethodAndRoleId(String apiPath, String httpMethod, Integer roleId) {
        String hql = "FROM ApiPermission ap WHERE ap.apiPath = :apiPath AND ap.httpMethod = :httpMethod AND ap.roleId = :roleId";
        return getSession().createQuery(hql, ApiPermission.class)
                .setParameter("apiPath", apiPath)
                .setParameter("httpMethod", httpMethod)
                .setParameter("roleId", roleId)
                .uniqueResultOptional();
    }

    @Override
    public List<ApiPermission> findByRoleId(Integer roleId) {
        String hql = "FROM ApiPermission ap WHERE ap.roleId = :roleId";
        return getSession().createQuery(hql, ApiPermission.class)
                .setParameter("roleId", roleId)
                .list();
    }

    @Override
    public List<ApiPermission> findByApiPathAndHttpMethod(String apiPath, String httpMethod) {
        String hql = "FROM ApiPermission ap WHERE ap.apiPath = :apiPath AND ap.httpMethod = :httpMethod";
        return getSession().createQuery(hql, ApiPermission.class)
                .setParameter("apiPath", apiPath)
                .setParameter("httpMethod", httpMethod)
                .list();
    }
} 