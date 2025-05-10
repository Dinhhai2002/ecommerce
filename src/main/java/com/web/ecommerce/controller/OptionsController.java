package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.Options;
import com.web.ecommerce.response.OptionsResponse;

@RestController
@RequestMapping("/api/v1/options")
public class OptionsController extends BaseController<Options, OptionsResponse, Integer> {
    public OptionsController() {
        super(OptionsResponse::new);
    }
}
