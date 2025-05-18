package com.web.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.response.ProductImageResponse;

@RestController
@RequestMapping("/api/v1/product-images")
public class ProductImageController extends BaseController<ProductImage, ProductImageResponse, Integer> {
    public ProductImageController() {
        super(ProductImageResponse::new);
    }
    
}
