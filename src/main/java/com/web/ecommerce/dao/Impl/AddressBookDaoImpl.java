package com.web.ecommerce.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.ecommerce.dao.AddressBookDao;
import com.web.ecommerce.entity.AddressBook;

@Repository("AddressBookDao")
@Transactional
public class AddressBookDaoImpl extends BaseDaoImpl<AddressBook, Integer> implements AddressBookDao {
    public AddressBookDaoImpl() {
        super(AddressBook.class);
    }

    @Override
    public AddressBook findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<AddressBook> query = builder.createQuery(AddressBook.class);
        Root<AddressBook> root = query.from(AddressBook.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
