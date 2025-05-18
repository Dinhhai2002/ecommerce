package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.OrderDetailDao;
import com.web.ecommerce.entity.OrderDetail;

@Repository("OrderDetailDao")
@Transactional
public class OrderDetailDaoImpl extends BaseDaoImpl<OrderDetail, Integer> implements OrderDetailDao {
    public OrderDetailDaoImpl() {
        super(OrderDetail.class);
    }

    @Override
    public OrderDetail findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OrderDetail> query = builder.createQuery(OrderDetail.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OrderDetail> query = builder.createQuery(OrderDetail.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        query.where(builder.equal(root.get("orderId"), orderId));
        return this.getSession().createQuery(query).getResultList();
    }

}
