package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CartItemDao;
import com.web.ecommerce.entity.CartItem;

@Repository("CartItemDao")
@Transactional
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, Integer> implements CartItemDao {
    public CartItemDaoImpl() {
        super(CartItem.class);
    }

    @Override
    public CartItem findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<CartItem> query = builder.createQuery(CartItem.class);
        Root<CartItem> root = query.from(CartItem.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<CartItem> findByCartId(int cartId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<CartItem> query = builder.createQuery(CartItem.class);
        Root<CartItem> root = query.from(CartItem.class);
        query.where(builder.equal(root.get("cartId"), cartId));
        return this.getSession().createQuery(query).getResultList();
    }
}
