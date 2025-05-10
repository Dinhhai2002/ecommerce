package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.response.OptionValuesResponse;

@RestController
@RequestMapping("/api/v1/option-values")
public class OptionValuesController extends BaseController<OptionValues, OptionValuesResponse, Integer> {
    public OptionValuesController() {
        super(OptionValuesResponse::new);
    }
}
