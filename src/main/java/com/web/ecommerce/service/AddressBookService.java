package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface AddressBookService extends BaseService<AddressBook, Integer> {
    AddressBook findByName(String name);
    StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status, Pagination pagination) throws Exception;
    List<AddressBook> findByUserId(int userId) throws Exception;
    void setDefaultAddress(int userId, int addressId) throws Exception;
}
