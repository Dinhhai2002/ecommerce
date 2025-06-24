package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.ecommerce.dao.ApiPermissionDao;
import com.web.ecommerce.entity.ApiPermission;
import com.web.ecommerce.service.ApiPermissionService;

@Service
public class ApiPermissionServiceImpl extends BaseServiceImpl<ApiPermission, Integer> implements ApiPermissionService {

    @Autowired
    private ApiPermissionDao apiPermissionDao;

    @Override
    public boolean hasPermission(String apiPath, String httpMethod, Integer roleId) {
        return apiPermissionDao.findByApiPathAndHttpMethodAndRoleId(apiPath, httpMethod, roleId).isPresent();
    }

    @Override
    public List<ApiPermission> getPermissionsByRoleId(Integer roleId) {
        return apiPermissionDao.findByRoleId(roleId);
    }

    @Override
    public List<ApiPermission> getPermissionsByApiPathAndMethod(String apiPath, String httpMethod) {
        return apiPermissionDao.findByApiPathAndHttpMethod(apiPath, httpMethod);
    }

    @Override
    public ApiPermission createPermission(String apiPath, String httpMethod, Integer roleId, String description) {
        ApiPermission permission = new ApiPermission(apiPath, httpMethod, roleId, description);
        create(permission);
        return permission;
    }
} 