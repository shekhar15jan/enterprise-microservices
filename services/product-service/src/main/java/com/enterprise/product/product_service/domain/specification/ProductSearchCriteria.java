package com.enterprise.product.product_service.domain.specification;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSearchCriteria {
    private String text;      // search in name/description
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Integer maxStock;
}

