package com.enterprise.product.product_service.exceptions;

public abstract class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
    public abstract String getErrorCode();
    public abstract String getTitle();
}
