package com.enterprise.product.product_service.exceptions;

import java.util.UUID;

public class ProductNotFoundException extends NotFoundException{
    private final UUID productId;
    public ProductNotFoundException(UUID publicId) {
        super("Product with id "+ publicId+" not found"); //404
        this.productId = publicId;
    }
    public UUID getProductId() {
        return productId;
    }

    @Override
    public String getErrorCode() {
        return "PRD-SVC-4041";
    }

    @Override
    public String getTitle() {
        return "Product Not Found";
    }
}
