package com.web.ecommerce.dao;

import com.web.ecommerce.entity.AddressBook;

public interface AddressBookDao extends BaseDao<AddressBook, Integer> {
    AddressBook findByName(String name);
}