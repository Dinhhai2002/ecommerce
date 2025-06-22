package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.OrderStatusLogDao;
import com.web.ecommerce.entity.OrderStatusLog;

@Repository("OrderStatusLogDao")
@Transactional
public class OrderStatusLogDaoImpl extends BaseDaoImpl<OrderStatusLog, Integer> implements OrderStatusLogDao {
    public OrderStatusLogDaoImpl() {
        super(OrderStatusLog.class);
    }

    @Override
    public OrderStatusLog findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OrderStatusLog> query = builder.createQuery(OrderStatusLog.class);
        Root<OrderStatusLog> root = query.from(OrderStatusLog.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<OrderStatusLog> findByOrderId(int orderId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<OrderStatusLog> query = builder.createQuery(OrderStatusLog.class);
        Root<OrderStatusLog> root = query.from(OrderStatusLog.class);
        query.where(builder.equal(root.get("orderId"), orderId));
        return this.getSession().createQuery(query).getResultList();
    }

}
