package com.enterprise.product.product_service.domain.service;

import com.enterprise.product.product_service.domain.entity.Product;
import com.enterprise.product.product_service.web.dto.ProductDto;

public class ProductMapper {
    //ProductDto -> product
    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .publicId(product.getPublicId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
    //Product -> productDto
    public static Product toProduct(ProductDto productDto) {
        return Product.builder()
                .publicId(productDto.publicId())
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .stockQuantity(productDto.stockQuantity())
                .build();
    }
}
