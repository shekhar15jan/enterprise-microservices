package com.enterprise.product.product_service.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(
        UUID publicId,
        String name,
        String description,
        BigDecimal price,
        Integer stock   Quantity,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
){
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private UUID publicId;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

        public Builder publicId(UUID publicId){
            this.publicId = publicId;
            return this;
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder description(String description){
            this.description = description;
            return this;
        }
        public Builder price(BigDecimal price){
            this.price = price;
            return this;
        }
        public Builder stockQuantity(Integer stockQuantity){
            this.stockQuantity = stockQuantity;
            return this;
        }
        public Builder createdDate(LocalDateTime createdDate){
            this.createdDate = createdDate;
            return this;
        }
        public Builder updatedDate(LocalDateTime updatedDate){
            this.updatedDate = updatedDate;
            return this;
        }
        public ProductDto build(){
            return new ProductDto(publicId,name,description,price,stockQuantity,createdDate,updatedDate);
        }
    }


}
