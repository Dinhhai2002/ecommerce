package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.AddressBookDao;
import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.service.AddressBookService;

@Service("AddressBookService")
@Transactional(rollbackFor = Error.class)
public class AddressBookServiceImpl extends BaseServiceImpl<AddressBook, Integer> implements AddressBookService {
    @Autowired
    private AddressBookDao addressBookDao;

    @Override
    public AddressBook findByName(String name) {
        return addressBookDao.findByName(name);
    }
}
