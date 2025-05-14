package com.web.ecommerce.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.BaseDao;
import com.web.ecommerce.model.StoreProcedureListResult;

public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
    @Autowired
    private SessionFactory sessionFactory;

    private final Class<T> persistentClass;

    public BaseDaoImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(T entity) {
        getSession().save(entity);
    }

    @Override
    public T findOne(ID id) {
        return getSession().find(persistentClass, id);
    }

    @Override
    public void update(T entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        Root<T> root = query.from(persistentClass);
        query.select(root);
        return getSession().createQuery(query).getResultList();
    }

    @Override
    public List<T> findByIds(List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        Root<T> root = query.from(persistentClass);
        query.select(root).where(root.get("id").in(ids));
        return getSession().createQuery(query).getResultList();
    }

    @Override
    public StoreProcedureListResult<T> getListWithCondition(
            String keySearch,
            int status,
            Pagination pagination) throws Exception {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        Root<T> root = query.from(persistentClass);

        List<Predicate> predicates = new ArrayList<>();
        // Default: keySearch trên trường "name" nếu có
        if (keySearch != null && !keySearch.trim().isEmpty()) {
            try {
                persistentClass.getDeclaredField("name");
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + keySearch.toLowerCase() + "%"));
            } catch (NoSuchFieldException e) {
                // Nếu không có trường name thì bỏ qua
            }
        }
        // status trên trường "status" nếu có
        if (status >= 0) {
            try {
                persistentClass.getDeclaredField("status");
                predicates.add(builder.equal(root.get("status"), status));
            } catch (NoSuchFieldException e) {
                // Nếu không có trường status thì bỏ qua
            }
        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.orderBy(builder.desc(root.get("id")));

        javax.persistence.TypedQuery<T> typedQuery = getSession().createQuery(query);
        int totalRecord = typedQuery.getResultList().size();
        if (pagination != null) {
            typedQuery.setFirstResult(pagination.getOffset());
            typedQuery.setMaxResults(pagination.getLimit());
        }
        List<T> result = typedQuery.getResultList();
        return new StoreProcedureListResult<>(0, "Success", totalRecord, result);
    }
}
