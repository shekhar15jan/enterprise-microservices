package com.enterprise.product.product_service.exceptions;

public class ApiError{
    public static final String INSTANCE = "instance";
    public static final String TIMESTAMP = "timestamp";
    public static final String TRACE_ID = "traceId";
    public static final String ERROR_CODE = "errorCode";
    private ApiError(){} //to prevent instantiation
}
