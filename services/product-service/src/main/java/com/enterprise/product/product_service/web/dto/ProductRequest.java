package com.enterprise.product.product_service.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name is required")
        @Size(max=150)
        String name,
        @Size(max=1000)
        String description,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.1", inclusive = true, message = "price must be > =0.01")
        BigDecimal price,

        @NotNull(message = "stockQuantity is required")
        @Min(value = 0, message = "stockQuantity must be >= 0")
        Integer stockQuantity

        ) {
}
