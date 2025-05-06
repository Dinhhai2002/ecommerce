package com.web.ecommerce.controller;

import com.web.ecommerce.entity.Category;
import com.web.ecommerce.response.CategoryResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController extends BaseController<Category, CategoryResponse, Integer> {
    public CategoryController() {
        super(CategoryResponse::new);
    }
}
