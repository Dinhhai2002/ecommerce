package com.web.ecommerce.service;

import com.web.ecommerce.entity.AddressBook;

public interface AddressBookService extends BaseService<AddressBook, Integer> {
    AddressBook findByName(String name);
}
