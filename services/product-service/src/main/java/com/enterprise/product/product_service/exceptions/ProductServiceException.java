package com.enterprise.product.product_service.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String message, DataIntegrityViolationException e) {
        super(message);
    }
}
