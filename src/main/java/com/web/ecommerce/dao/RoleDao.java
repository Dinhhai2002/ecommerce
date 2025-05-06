package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Role;
import java.util.List;

public interface RoleDao extends BaseDao<Role, Integer> {
    List<String> findRoleNamesByIds(List<Integer> ids);
}
