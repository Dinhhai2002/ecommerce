package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.ProductOptions;
import com.web.ecommerce.response.ProductOptionsResponse;

@RestController
@RequestMapping("/api/v1/product-options")
public class ProductOptionsController extends BaseController<ProductOptions, ProductOptionsResponse, Integer> {
    public ProductOptionsController() {
        super(ProductOptionsResponse::new);
    }
}
