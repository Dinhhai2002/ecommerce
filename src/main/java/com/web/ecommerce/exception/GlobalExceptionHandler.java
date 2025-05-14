package com.web.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.web.ecommerce.response.BaseResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ProductNotFoundException
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with NOT_FOUND status
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessageError(ex.getMessage());
        response.setData(null);
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle general exceptions
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessageError(ex.getMessage());
        response.setData(null);
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 