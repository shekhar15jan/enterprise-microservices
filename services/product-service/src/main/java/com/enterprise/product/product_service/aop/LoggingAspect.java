package com.enterprise.product.product_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.enterprise.product.product_service.web.controller..*)")
    public void controllerMethods() {}

    @Pointcut("within(com.enterprise.product.product_service.domain.service.impl..*)")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - startTime;
        log.info("Service [{}] executed in {} ms", joinPoint.getSignature(), elapsed);
        return result;
    }

    // Before advice to log controller request
    @Before("controllerMethods() && @annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logControllerRequest(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);  // Add traceId to MDC for logging correlation
        Object[] args = joinPoint.getArgs();
        log.info("traceId={} - Incoming request to [{}] with args={}",
                traceId,
                joinPoint.getSignature().toShortString(),
                Arrays.toString(args));
    }

    // After returning advice to log controller response
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logControllerResponse(JoinPoint joinPoint, Object result) {
        log.info("traceId={} - Response from [{}] = {}", MDC.get("traceId"), joinPoint.getSignature(), result);
        MDC.clear();  // Clear traceId after request completes
    }

    // After throwing advice for exception logging
    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        log.error("traceId={} - Exception in [{}]: {}", MDC.get("traceId"), joinPoint.getSignature(), ex.getMessage(), ex);
        MDC.clear();
    }

}
