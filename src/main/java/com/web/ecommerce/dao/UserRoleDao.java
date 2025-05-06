package com.web.ecommerce.dao;

import com.web.ecommerce.entity.UserRole;
import java.util.List;

public interface UserRoleDao extends BaseDao<UserRole, Integer> {
    List<UserRole> findByUserId(Integer userId);
    List<Integer> findRoleIdsByUserId(Integer userId);
}
