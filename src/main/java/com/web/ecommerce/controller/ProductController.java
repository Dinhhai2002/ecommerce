package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.Product;
import com.web.ecommerce.response.ProductResponse;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController extends BaseController<Product, ProductResponse, Integer> {
    public ProductController() {
        super(ProductResponse::new);
    }
}
