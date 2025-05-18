package com.web.ecommerce.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.CartDao;
import com.web.ecommerce.entity.Cart;

@Repository("CartDao")
@Transactional
public class CartDaoImpl extends BaseDaoImpl<Cart, Integer> implements CartDao {
    public CartDaoImpl() {
        super(Cart.class);
    }

    @Override
    public Cart findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Cart> query = builder.createQuery(Cart.class);
        Root<Cart> root = query.from(Cart.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Cart> findByUserId(int userId) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Cart> query = builder.createQuery(Cart.class);        
        Root<Cart> root = query.from(Cart.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
    }
        
}
