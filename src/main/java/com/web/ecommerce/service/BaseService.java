package com.web.ecommerce.service;

import java.io.Serializable;
import java.util.List;
import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface BaseService<T, ID extends Serializable> {
    void create(T entity);
    T findOne(ID id);
    void update(T entity);
    void delete(T entity);
    List<T> getAll();
    List<T> findByIds(List<ID> ids);
    StoreProcedureListResult<T> getListWithCondition(String keySearch, int status, Pagination pagination) throws Exception;
}
