package com.web.ecommerce.dao;

import java.util.List;
import java.util.Optional;

import com.web.ecommerce.entity.ApiPermission;

public interface ApiPermissionDao extends BaseDao<ApiPermission, Integer> {
    
    /**
     * Tìm quyền theo api path, http method và role id
     */
    Optional<ApiPermission> findByApiPathAndHttpMethodAndRoleId(String apiPath, String httpMethod, Integer roleId);
    
    /**
     * Lấy danh sách quyền theo role id
     */
    List<ApiPermission> findByRoleId(Integer roleId);
    
    /**
     * Lấy danh sách quyền theo api path và http method
     */
    List<ApiPermission> findByApiPathAndHttpMethod(String apiPath, String httpMethod);
} 