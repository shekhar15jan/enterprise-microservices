package com.enterprise.product.product_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String BASE_URL= "/errors/";
    private final MessageSource messageSource;
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ProductServiceException.class)
    public ProblemDetail handleProductServiceException(ProductServiceException ex, WebRequest request) {
        log.error("Service Exception {}",ex.toString());
        String title = messageSource.getMessage("title.product.service", null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage("error.product.service", null, LocaleContextHolder.getLocale());
        String type = messageSource.getMessage("type.product.service", null, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(BASE_URL.concat(type)));
        problemDetail.setProperty(ApiError.INSTANCE,request.getDescription(false));
        problemDetail.setProperty(ApiError.TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(ApiError.TRACE_ID, MDC.get("traceId"));
        problemDetail.setProperty(ApiError.ERROR_CODE,"PRD-SVC-5001"); //tels its product service with error code 500
        return problemDetail;
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExistsException(ProductAlreadyExistsException ex, WebRequest request) {
        log.error("Product already exist Exception {}",ex.toString());
        String title = messageSource.getMessage("title.product.exists", null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage("error.product.exists", null, LocaleContextHolder.getLocale());
        String type = messageSource.getMessage("type.product.service", null, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(BASE_URL.concat(type)));
        problemDetail.setProperty(ApiError.INSTANCE,request.getDescription(false));
        problemDetail.setProperty(ApiError.TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(ApiError.TRACE_ID, MDC.get("traceId"));
        problemDetail.setProperty(ApiError.ERROR_CODE,"PRD-SVC-4091");
        return problemDetail;
    }
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.error("Not Found Exception {}",ex.toString());
        String type = messageSource.getMessage("type.product.notfound", null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage();
        String title = messageSource.getMessage("title.resource.notfound", null, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(BASE_URL.concat(type)));
        problemDetail.setProperty(ApiError.INSTANCE,request.getDescription(false));
        problemDetail.setProperty(ApiError.TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(ApiError.TRACE_ID, MDC.get("traceId"));
        problemDetail.setProperty(ApiError.ERROR_CODE, ex.getMessage());

        if(ex instanceof ProductNotFoundException productNotFoundException) {
            problemDetail.setProperty("productId",productNotFoundException.getProductId());
        }else if(ex instanceof ResourceNotFoundException resourceNotFoundException) {
            problemDetail.setProperty("resourceId",resourceNotFoundException.getResourceId());
            problemDetail.setProperty("resourceName",resourceNotFoundException.getResourceName());
        }
        return problemDetail;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Method argument not valid Exception {}",ex.toString());
        String title = messageSource.getMessage("title.parameter.invalid", null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage("error.parameter.invalid", null, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(BASE_URL.concat("error")));
        problemDetail.setProperty(ApiError.INSTANCE,request.getDescription(false));
        problemDetail.setProperty(ApiError.TIMESTAMP, LocalDateTime.now());
        problemDetail.setProperty(ApiError.TRACE_ID, MDC.get("traceId"));
        problemDetail.setProperty(ApiError.ERROR_CODE,"PRD-SVC-5001");

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1  // in case of duplicates
                ));
        problemDetail.setProperty("fieldErrors",fieldErrors);

        return problemDetail;
    }
}
