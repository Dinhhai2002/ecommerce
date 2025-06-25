package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.AddressBookDao;
import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.model.StoreProcedureListResult;
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

	@Override
	public StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status,
			Pagination pagination) throws Exception {
		return addressBookDao.spGListAddressBook(userId, keySearch, status, pagination);
	}

	@Override
	public List<AddressBook> findByUserId(int userId) throws Exception {
		 return addressBookDao.findByUserId(userId);
	}

	@Override
	public void setDefaultAddress(int userId, int addressId) throws Exception {
		  addressBookDao.setDefaultAddress(userId, addressId);
	}
}
