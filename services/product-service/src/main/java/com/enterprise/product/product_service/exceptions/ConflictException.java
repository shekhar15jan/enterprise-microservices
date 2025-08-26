package com.enterprise.product.product_service.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message); //409
    }
}