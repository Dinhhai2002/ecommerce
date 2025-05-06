package com.web.ecommerce.service.impl;

import com.web.ecommerce.dao.BaseDao;
import com.web.ecommerce.service.BaseService;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.common.utils.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional(rollbackFor = Error.class)
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    @Autowired
    protected BaseDao<T, ID> baseDao;

    @Override
    public void create(T entity) {
        baseDao.create(entity);
    }

    @Override
    public T findOne(ID id) {
        return baseDao.findOne(id);
    }

    @Override
    public void update(T entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Override
    public List<T> getAll() {
        return baseDao.getAll();
    }

    @Override
    public List<T> findByIds(List<ID> ids) {
        return baseDao.findByIds(ids);
    }

    @Override
    public StoreProcedureListResult<T> getListWithCondition(String keySearch, int status, Pagination pagination) throws Exception {
        return baseDao.getListWithCondition(keySearch, status, pagination);
    }
}
