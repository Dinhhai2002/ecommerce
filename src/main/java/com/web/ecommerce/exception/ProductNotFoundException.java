package com.web.ecommerce.exception;

/**
 * Exception thrown when a product cannot be found
 */
public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(Integer productId) {
        super("Product not found with ID: " + productId);
    }
}