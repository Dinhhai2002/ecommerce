package com.web.ecommerce.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T, ID extends Serializable> {
    void create(T entity);
    T findOne(ID id);
    void update(T entity);
    void delete(T entity);
    List<T> getAll();
    List<T> findByIds(List<ID> ids);
    com.web.ecommerce.model.StoreProcedureListResult<T> getListWithCondition(
        String keySearch,
        int status,
        com.web.ecommerce.common.utils.Pagination pagination
    ) throws Exception;
}
