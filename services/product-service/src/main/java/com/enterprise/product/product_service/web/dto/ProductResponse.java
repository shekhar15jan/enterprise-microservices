package com.enterprise.product.product_service.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID publicId,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Long version,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {
}
