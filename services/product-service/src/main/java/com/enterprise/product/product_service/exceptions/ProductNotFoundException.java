package com.enterprise.product.product_service.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message); //404
    }
}
