package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.Option;
import com.web.ecommerce.response.OptionResponse;

@RestController
@RequestMapping("/api/v1/options")
public class OptionController extends BaseController<Option, OptionResponse, Integer> {
    public OptionController() {
        super(OptionResponse::new);
    }
}
