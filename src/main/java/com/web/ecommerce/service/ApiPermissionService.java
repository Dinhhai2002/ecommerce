package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.ApiPermission;

public interface ApiPermissionService extends BaseService<ApiPermission, Integer> {
    
    /**
     * Kiểm tra quyền truy cập API
     */
    boolean hasPermission(String apiPath, String httpMethod, Integer roleId);
    
    /**
     * Lấy danh sách quyền theo role id
     */
    List<ApiPermission> getPermissionsByRoleId(Integer roleId);
    
    /**
     * Lấy danh sách quyền theo api path và http method
     */
    List<ApiPermission> getPermissionsByApiPathAndMethod(String apiPath, String httpMethod);
    
    /**
     * Tạo quyền mới
     */
    ApiPermission createPermission(String apiPath, String httpMethod, Integer roleId, String description);
} 