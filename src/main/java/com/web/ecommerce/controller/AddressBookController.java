package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.response.AddressBookResponse;

@RestController
@RequestMapping("/api/v1/address-book")
public class AddressBookController extends BaseController<AddressBook, AddressBookResponse, Integer> {
    public AddressBookController() {
        super(AddressBookResponse::new);
    }
}
