package com.enterprise.product.product_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String BASE_URL= "/errors/";

    @ExceptionHandler(ProductServiceException.class)
    public ProblemDetail handleProductServiceException(ProductServiceException ex, WebRequest request) {
        log.error("Service Exception {}",ex.toString());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occur while processing the request");
        problemDetail.setTitle("Product Service Error");
        problemDetail.setType(URI.create(BASE_URL+"service-error"));
        problemDetail.setProperty("instance",request.getDescription(false));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("errorCode","PRD-SVC-5001"); //tels its product service with error code 500
        return problemDetail;
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExistsException(ProductAlreadyExistsException ex, WebRequest request) {
        log.error("Product already exist Exception {}",ex.toString());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Product already exists");
        problemDetail.setTitle("Product Already Exists");
        problemDetail.setType(URI.create(BASE_URL+"product-already-exists"));
        problemDetail.setProperty("instance",request.getDescription(false));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("errorCode","PRD-SVC-4091");
        return problemDetail;
    }

    /*@ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Product Not Found");
        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(URI.create(BASE_URL+"product-not-found"));
        problemDetail.setProperty("instance",request.getDescription(false));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("errorCode","PRD-SVC-4041");
        return problemDetail;
    }*/
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.error("Not Found Exception {}",ex.toString());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(ex.getTitle());
        problemDetail.setType(URI.create(BASE_URL+"not-found"));
        problemDetail.setProperty("instance",request.getDescription(false));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("errorCode",ex.getMessage());

        if(ex instanceof ProductNotFoundException productNotFoundException) {
            problemDetail.setProperty("productId",productNotFoundException.getProductId());
        }else if(ex instanceof ResourceNotFoundException resourceNotFoundException) {
            problemDetail.setProperty("resourceId",resourceNotFoundException.getResourceId());
            problemDetail.setProperty("resourceName",resourceNotFoundException.getResourceName());
        }

       /* switch (ex.getClass().getSimpleName()) {
            case "ProductNotFoundException"->{
                ProductNotFoundException exception = (ProductNotFoundException) ex;
                problemDetail.setProperty("productId",exception.getProductId());
            }
            case "ResourceNotFoundException" -> {
                ResourceNotFoundException exception = (ResourceNotFoundException) ex;
                problemDetail.setProperty("resourceId",exception.getResourceId());
                problemDetail.setProperty("resourceName",exception.getResourceName());
            }
        }*/
        return problemDetail;
    }
}
